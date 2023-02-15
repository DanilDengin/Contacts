package com.example.lessons.contactList.presentation

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import com.example.library.R
import org.junit.After
import org.junit.Before
import org.junit.Test

@LargeTest
internal class ContactListFragmentTest {

//    @Inject
//    lateinit var contactsRepository: ContactsRepository

    private  var scenario: FragmentScenario<ContactListFragment>? = null

    private var idlingResource: IdlingResource? = null

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_Lessons)
        scenario?.moveToState(Lifecycle.State.STARTED)
        scenario?.onFragment{ fragment ->
            idlingResource = fragment.idlingResource
            IdlingRegistry.getInstance().register(idlingResource)
        }
//        DaggerAppComponentTest.factory()
//            .create()
//            .inject(this)
//        coEvery { contactsRepository.getShortContactsDetails() } returns contactListTestAndroid
    }

    @After
    fun tearDown() {
        scenario?.close()
        if(idlingResource !=null){
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
    }

    @Test
    fun test() {
        Espresso.onView(ViewMatchers.withId(R.id.contactListRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(9))

        val nameText = "Sasha"

        Espresso.onView(ViewMatchers.withText(nameText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

//        val hind = "Danil"
//        Espresso.onView(ViewMatchers.withText("Danil"))
//            .check(ViewAssertions.matches(ViewMatchers.withText(hind)))
    }
}