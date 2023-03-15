package com.nicolas.stores.editModule

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.nicolas.stores.R
import com.nicolas.stores.StoreApplication
import com.nicolas.stores.common.entities.StoreEntity
import com.nicolas.stores.databinding.FragmentEditStoreBinding
import com.nicolas.stores.editModule.viewModel.EditStoreViewModel
import com.nicolas.stores.mainModule.MainActivity
import java.util.concurrent.LinkedBlockingQueue


class EditStoreFragment : Fragment() {

    private lateinit var mBinding: FragmentEditStoreBinding
    //MVVM
    private lateinit var mEditStoreViewModel: EditStoreViewModel

    private var mActivity: MainActivity? = null
    private var mIsEditMode: Boolean = false
    private lateinit var mStoreEntity: StoreEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mEditStoreViewModel = ViewModelProvider(requireActivity()).get(EditStoreViewModel::class.java)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        mBinding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //MVVM
        setupViewModel()

        setupTextFields()
    }

    private fun setupViewModel() {
        mEditStoreViewModel.getStoreSelected().observe(viewLifecycleOwner) {

            mStoreEntity = it

           if (it.id != 0L) {
               mIsEditMode = true
               setUiStore(it)
           } else {
               mIsEditMode = false
           }
            setupActionBar()
       }
        mEditStoreViewModel.getResult().observe(viewLifecycleOwner) { result ->

            hideKeyboard()

            when(result) {
                is StoreEntity -> {
                    val msgRes = if (result.id == 0L) R.string.edit_store_message_save_success
                                 else R.string.edit_store_message_update_success

                    mEditStoreViewModel.setStoreSelected(mStoreEntity)

                    Snackbar.make(mBinding.root,
                        msgRes,
                        Snackbar.LENGTH_SHORT).show()

                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = if (mIsEditMode) getString(R.string.edit_store_title_edit)
                                             else getString(R.string.edit_store_title_add)

        setHasOptionsMenu(true)
    }

    private fun setupTextFields() {
        with(mBinding) {
            etName.addTextChangedListener { validateFields(tilName) }
            etPhone.addTextChangedListener {
                validateFields(tilPhone)
                validatePhoneNumber(tilPhone)
            }
            etPhotoUrl.addTextChangedListener {
                validateFields(tilPhotoUrl)
                loadImage(it.toString().trim())
            }
        }
    }

    private fun loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imgPhoto)
    }

    private fun setUiStore(storeEntity: StoreEntity) {
        with(mBinding) {
            etName.setText(storeEntity.name)
            etPhone.setText(storeEntity.phone)
            etWebsite.setText(storeEntity.website)
            etPhotoUrl.setText(storeEntity.photoUrl)
        }
    }

//    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                MaterialAlertDialogBuilder(requireActivity())
//                    .setTitle(R.string.dialog_exit_title)
//                    .setMessage(R.string.dialog_exit_message)
//                    .setPositiveButton(R.string.dialog_exit_ok) { _, _ ->
//                        if (isEnabled) {
//                            isEnabled = false
//                            requireActivity().onBackPressedDispatcher.onBackPressed()
//                        }
//                    }
//                    .setNegativeButton(R.string.dialog_delete_cancel, null)
//                    .show()
//            }
//        })
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {

            android.R.id.home -> {
                hideKeyboard()
                requireActivity().onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_save -> {
                if (validateFields(mBinding.tilPhotoUrl, mBinding.tilPhone, mBinding.tilName) &&
                    validatePhoneNumber(mBinding.tilPhone)) {

                    with(mStoreEntity) {
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        website = mBinding.etWebsite.text.toString().trim()
                        photoUrl = mBinding.etPhotoUrl.text.toString().trim()
                    }

                    if (mIsEditMode) mEditStoreViewModel.updateStore(mStoreEntity)
                    else mEditStoreViewModel.saveStore(mStoreEntity)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateFields(vararg textFields: TextInputLayout): Boolean {
        var isValid = true

        for (textField in textFields) {
            if (textField.editText?.text.toString().trim().isEmpty()) {
                textField.error = getString(R.string.helper_required)
                textField.editText?.requestFocus()
                isValid = false
            } else textField.error = null
        }

        return isValid
    }

    private fun validatePhoneNumber(phone: TextInputLayout): Boolean {
        var isValid = true

        if (phone.editText?.text.toString().trim().length != 12) {
            phone.error = getString(R.string.helper_invalid_phone)
            isValid = false
        } else phone.error = null

        return isValid
    }

    private fun hideKeyboard() {
        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        mEditStoreViewModel.setShowFab(true)
        mEditStoreViewModel.setResult(Any())

        setHasOptionsMenu(false)
        super.onDestroy()
    }
}