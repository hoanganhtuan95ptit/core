//package com.simple.coreapp.ui.dialogs.options
//
//import android.os.Bundle
//import android.view.View
//import androidx.core.os.bundleOf
//import androidx.fragment.app.setFragmentResult
//import androidx.recyclerview.widget.GridLayoutManager
//import com.simple.adapter.MultiAdapter
//import com.simple.core.utils.extentions.toArrayList
//import com.simple.coreapp.databinding.DialogListBinding
//import com.simple.coreapp.ui.base.dialogs.sheet.BaseViewBindingSheetFragment
//import com.simple.coreapp.ui.dialogs.options.adapters.OptionAdapter
//import com.simple.coreapp.ui.dialogs.options.adapters.OptionViewItem
//import com.simple.coreapp.utils.autoCleared
//import com.simple.coreapp.utils.ext.getSerializableListOrNull
//import com.simple.coreapp.utils.ext.getStringOrEmpty
//
//class OptionDialog : BaseViewBindingSheetFragment<DialogListBinding>() {
//
//    private var adapter by autoCleared<MultiAdapter>()
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setUpRecyclerView()
//    }
//
//    private fun setUpRecyclerView() {
//
//        val binding = binding ?: return
//
//        val optionAdapter = OptionAdapter(onItemClick = { view, item ->
//
//            setFragmentResult(arguments.getStringOrEmpty(Param.KEY_REQUEST), bundleOf(Param.DATA to item.id))
//            dismiss()
//        })
//
//        adapter = MultiAdapter(
//            optionAdapter
//        ).apply {
//
//            setRecyclerView(binding.recyclerView, GridLayoutManager(requireContext(), 2))
//        }
//
//        arguments?.getSerializableListOrNull<OptionViewItem>(Param.DATA)?.let {
//
//            adapter?.submitList(it)
//        }
//    }
//
//    companion object {
//
//        fun newInstance(keyRequest: String, viewItemList: List<OptionViewItem>) = OptionDialog().apply {
//
//            arguments = bundleOf(
//                Param.DATA to viewItemList.toArrayList(),
//                Param.KEY_REQUEST to keyRequest
//            )
//        }
//    }
//}
//
//object Param {
//    const val PATH = "PATH"
//    const val DATA = "DATA"
//    const val INPUT = "INPUT"
//    const val OUTPUT = "OUTPUT"
//    const val LANGUAGE = "LANGUAGE"
//    const val PERMISSION = "PERMISSION"
//    const val KEY_REQUEST = "KEY_REQUEST"
//    const val LANGUAGE_CODE = "LANGUAGE_CODE"
//    const val LANGUAGE_INPUT = "LANGUAGE_INPUT"
//    const val LANGUAGE_OUTPUT = "LANGUAGE_OUTPUT"
//}