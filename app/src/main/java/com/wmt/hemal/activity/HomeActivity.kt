package com.wmt.hemal.activity

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.wmt.hemal.R
import com.wmt.hemal.adapter.UserAdapter
import com.wmt.hemal.model.User
import com.wmt.hemal.model.UserList
import com.wmt.hemal.network.RetrofitClient
import com.wmt.hemal.shared_preference.PreferenceUtils
import com.wmt.hemal.utility.Logger
import com.wmt.hemal.utility.PaginationScrollListener
import com.wmt.hemal.utility.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : BaseActivity() {
    private var page = 1

    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    private lateinit var toolBar: MaterialToolbar
    private lateinit var tvTitle: MaterialTextView
    private lateinit var rvUsers: RecyclerView

    private val headerMap = HashMap<String, String>()

    //private var users: List<User> = ArrayList()
    private var users = ArrayList<User>()
    private lateinit var userAdapter: UserAdapter

    override fun layoutResourceId(): Int {
        return R.layout.activity_home
    }

    @SuppressLint("SetTextI18n")
    override fun init() {
        //region Toolbar Initialization
        toolBar = findViewById(R.id.toolBar)
        tvTitle = findViewById(R.id.tvTitle)

        setSupportActionBar(toolBar)

        tvTitle.text = "Hello ${PreferenceUtils.getUserName(this)}"
        //endregion

        val layoutManager = LinearLayoutManager(baseContext)

        rvUsers = findViewById(R.id.rvUsers)
        rvUsers.layoutManager = layoutManager
        rvUsers.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true

                page += 1

                apiLoadMoreUserList()
            }
        })

        headerMap["Authorization"] = "Bearer ${PreferenceUtils.getToken(this)}"

        apiUserList()
    }

    override fun getIntentData() {
    }

    override fun validate(): Boolean {
        return true
    }

    /***
     * Get User List
     */
    private fun apiUserList() {
        if (Utils.isOnline(this)) {
            showProgressDialog("")

            RetrofitClient
                .instance
                .userList(headerMap, page.toString())
                .enqueue(object : Callback<UserList> {
                    override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                        val userList: UserList? = response.body()

                        Logger.e("Response Code: " + response.code())

                        if (userList != null) {
                            if (userList.meta.status == "ok") {
                                //users = userList.data.users
                                users.addAll(userList.data.users)

                                userAdapter = UserAdapter(baseContext, users)
                                rvUsers.adapter = userAdapter

                                userAdapter.notifyDataSetChanged()
                            }
                        }

                        dismissProgressDialog()
                    }

                    override fun onFailure(call: Call<UserList>, t: Throwable) {
                        dismissProgressDialog()

                        Logger.e(t.toString())

                        handleApiError(t)
                    }

                })
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }

    /***
     * Load more user list
     */
    private fun apiLoadMoreUserList() {
        if (Utils.isOnline(this)) {
            RetrofitClient
                .instance
                .userList(headerMap, page.toString())
                .enqueue(object : Callback<UserList> {
                    override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                        val userList: UserList? = response.body()

                        Logger.e("Response Code: " + response.code())

                        if (userList != null) {
                            if (userList.meta.status == "ok") {
                                val size = users.size
                                val newSize = userList.data.users.size

                                users.addAll(userList.data.users)

                                userAdapter.notifyItemRangeChanged(size, newSize)
                            }

                            if (userList.data.pagination.page == userList.data.pagination.lastPage.toString()){
                                isLastPage = true
                            }
                        }

                        isLoading = false
                    }

                    override fun onFailure(call: Call<UserList>, t: Throwable) {
                        isLoading = false

                        Logger.e(t.toString())

                        handleApiError(t)
                    }

                })
        } else {
            showToast(getString(R.string.no_internet_connection))
        }
    }
}