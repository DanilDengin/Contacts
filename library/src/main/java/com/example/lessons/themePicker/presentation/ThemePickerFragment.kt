package com.example.lessons.themePicker.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.presentation.MainActivity
import com.example.library.R
import com.example.library.databinding.FragmentThemePickerBinding

internal class ThemePickerFragment : Fragment(R.layout.fragment_theme_picker), MenuProvider {

    private val binding by viewBinding(FragmentThemePickerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)
        mainActivity.supportActionBar?.setTitle(R.string.theme_picker_toolbar)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val radioButtonSystemThemeId = R.id.radioButtonPhoneTheme
        val radioButtonLightThemeId = R.id.radioButtonLightTheme
        val radioButtonNightThemeId = R.id.radioButtonNightTheme
        val themeDelegate = mainActivity.themeDelegate
        setSelectedButton(themeDelegate)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                radioButtonSystemThemeId -> themeDelegate.setSystemMode()
                radioButtonLightThemeId -> themeDelegate.setLightMode()
                radioButtonNightThemeId -> themeDelegate.setNightMode()
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.backstack_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }
            else -> false
        }
    }

    private fun setSelectedButton(themeDelegate: ThemeDelegate) {
        themeDelegate.setSelectedButton(
            { binding.radioButtonLightTheme.isChecked = true },
            { binding.radioButtonNightTheme.isChecked = true },
            { binding.radioButtonPhoneTheme.isChecked = true }
        )
    }
}
