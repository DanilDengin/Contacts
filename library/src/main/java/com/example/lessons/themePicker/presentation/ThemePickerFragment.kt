package com.example.lessons.themePicker.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.di.provider.ThemeDependenciesProvider
import com.example.lessons.presentation.mainActivity.MainActivity
import com.example.lessons.themePicker.di.DaggerThemePickerComponent
import com.example.library.R
import com.example.library.databinding.FragmentThemePickerBinding
import javax.inject.Inject

internal class ThemePickerFragment : Fragment(R.layout.fragment_theme_picker) {

    private val binding by viewBinding(FragmentThemePickerBinding::bind)

    @Inject
    lateinit var themeDelegate: ThemeDelegate

    override fun onAttach(context: Context) {
        DaggerThemePickerComponent.builder()
            .themeComponent((requireContext().applicationContext as ThemeDependenciesProvider).getThemeDependencies())
            .build()
            .also { it.inject(this) }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.also { mainActivity ->
            initActionBar(mainActivity)
            setSelectedButton(themeDelegate)
            binding.radioGroupThemePicker.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioButtonSystemTheme -> themeDelegate.setSystemMode()
                    R.id.radioButtonLightTheme -> themeDelegate.setLightMode()
                    R.id.radioButtonNightTheme -> themeDelegate.setNightMode()
                }
            }
        }
    }

    private fun initActionBar(mainActivity: MainActivity) {
        mainActivity.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.backstack_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem.itemId == android.R.id.home) {
                    parentFragmentManager.popBackStack()
                    true
                } else {
                    false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
        mainActivity.supportActionBar?.also { actionBar ->
            actionBar.setTitle(R.string.theme_picker_toolbar)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setSelectedButton(themeDelegate: ThemeDelegate?) {
        themeDelegate?.setThemeButton(
            { binding.radioButtonLightTheme.isChecked = true },
            { binding.radioButtonNightTheme.isChecked = true },
            { binding.radioButtonSystemTheme.isChecked = true }
        )
    }
}
