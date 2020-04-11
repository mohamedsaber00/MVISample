package com.example.myapplication.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.BlogPost
import com.example.myapplication.model.User
import com.example.myapplication.ui.DataStateListener
import com.example.myapplication.ui.main.state.MainStateEvent
import com.example.myapplication.util.TopSpacingItemDecotation
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), BlogListAdapter.Interaction {


    lateinit var viewModel: MainViewModel

    private val TAG = "MainFragment"

    lateinit var datastateHandler: DataStateListener

    lateinit var blogListAdapter: BlogListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = activity?.run { ViewModelProvider(this).get(MainViewModel::class.java) }
            ?: throw Exception("Invalid activity")
        subscribeObserver()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            val topSpacingItemDecotation = TopSpacingItemDecotation(30)
            addItemDecoration(topSpacingItemDecotation)
            blogListAdapter = BlogListAdapter(this@MainFragment)
            adapter = blogListAdapter
        }
    }


    fun subscribeObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { datastate ->
            //Handle loading and message
            datastateHandler.onDataStateChange(datastate)
            //Handle Data
            datastate.data?.let { event ->
                event.getContentIfNotHandled()?.let {
                    it.blogPosts?.let { blogPosts ->
                        viewModel.setBlogListData(blogPosts)
                    }
                    it.user?.let { user ->
                        //set user data
                        viewModel.setUser(user)
                    }
                }

            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.blogPosts?.let {list ->
                blogListAdapter.submitList(list)
            }
            viewState.user?.let {
                setUser(it)
            }
        })
    }

    private fun setUser(user: User){
        email.text = user.email
        username.text = user.username
        view?.let {
            Glide.with(this).load(user.image).into(image)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_get_user -> triggerGetUserEvent()
            R.id.action_get_blog -> triggerGetBlogEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun triggerGetBlogEvent() {
        viewModel.setStateEvent(MainStateEvent.GetBlogPostEvent())

    }

    private fun triggerGetUserEvent() {
        viewModel.setStateEvent(MainStateEvent.GetUserEvent("1"))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            datastateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            print("DEBUG : $context must implement  DataStateListener")

        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        println("DEBUG : CLICKED")
    }
}