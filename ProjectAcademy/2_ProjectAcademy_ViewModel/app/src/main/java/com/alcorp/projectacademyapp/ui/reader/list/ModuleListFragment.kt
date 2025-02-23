package com.alcorp.projectacademyapp.ui.reader.list

import android.content.Context
import android.os.Bundle
import android.service.voice.AlwaysOnHotwordDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alcorp.projectacademyapp.R
import com.alcorp.projectacademyapp.data.ModuleEntity
import com.alcorp.projectacademyapp.ui.reader.CourseReaderActivity
import com.alcorp.projectacademyapp.ui.reader.CourseReaderCallback
import com.alcorp.projectacademyapp.ui.reader.CourseReaderViewModel
import com.alcorp.projectacademyapp.utils.DataDummy
import kotlinx.android.synthetic.main.fragment_module_list.*

class ModuleListFragment : Fragment(), MyAdapterClickListener {

    companion object {
        val TAG = ModuleListFragment::class.java.simpleName

        fun newInstance(): ModuleListFragment {
            return ModuleListFragment()
        }
    }

    private lateinit var adapter: ModuleListAdapter
    private lateinit var courseReCallback: CourseReaderCallback
    private lateinit var viewModel : CourseReaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_module_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[CourseReaderViewModel::class.java]
        adapter = ModuleListAdapter(this)
        populateRecyclerView(viewModel.getModules())
    }

    private fun populateRecyclerView(modules: List<ModuleEntity>) {
        progress_bar.visibility = View.GONE
        adapter.setModule(modules)
        rv_module.layoutManager = LinearLayoutManager(context)
        rv_module.setHasFixedSize(true)
        rv_module.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(rv_module.context, DividerItemDecoration.VERTICAL)
        rv_module.addItemDecoration(dividerItemDecoration)
    }

    override fun onItemClicked(position: Int, moduleId: String) {
        courseReCallback.moveTo(position, moduleId)
        viewModel.setSelectedModule(moduleId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        courseReCallback = context as CourseReaderActivity
    }
}