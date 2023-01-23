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

internal class ThemePickerFragment : Fragment(R.layout.fragment_theme_picker) {

    private val binding by viewBinding(FragmentThemePickerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity: MainActivity? = activity as? MainActivity
        initializeActionBar(mainActivity)
        val themeDelegate = mainActivity?.themeDelegate
        setSelectedButton(themeDelegate)
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonSystemTheme -> themeDelegate?.setSystemMode()
                R.id.radioButtonLightTheme -> themeDelegate?.setLightMode()
                R.id.radioButtonNightTheme -> themeDelegate?.setNightMode()
            }
        }
    }

    private fun initializeActionBar(mainActivity: MainActivity?) {
        mainActivity?.also { activity ->
            activity.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.backstack_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return if (menuItem.itemId ==android.R.id.home) {
                        parentFragmentManager.popBackStack()
                        true
                    } else {
                        false
                    }
                }
            })
            activity.supportActionBar?.also { actionBar ->
                actionBar.setTitle(R.string.theme_picker_toolbar)
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setSelectedButton(themeDelegate: ThemeDelegate?) {
        themeDelegate?.setSelectedButton(
            { binding.radioButtonLightTheme.isChecked = true },
            { binding.radioButtonNightTheme.isChecked = true },
            { binding.radioButtonSystemTheme.isChecked = true }
        )
    }
}
