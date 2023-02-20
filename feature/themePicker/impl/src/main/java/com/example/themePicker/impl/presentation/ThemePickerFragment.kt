package com.example.themePicker.impl.presentation

import com.example.themePicker.impl.R as FutureRes
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.di.dependency.findFeatureExternalDeps
import com.example.mvvm.getRootViewModel
import com.example.themePicker.impl.databinding.FragmentThemePickerBinding
import com.example.ui.R
import javax.inject.Inject

class ThemePickerFragment : Fragment(FutureRes.layout.fragment_theme_picker) {

    private val binding by viewBinding(FragmentThemePickerBinding::bind)

    @Inject
    lateinit var themeDelegate: ThemeDelegate

    override fun onAttach(context: Context) {
        ThemeFeatureComponentDependenciesProvider.featureDependencies = findFeatureExternalDeps()
        getRootViewModel<ThemeComponentViewModel>().component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.theme_picker_toolbar)
        setSelectedButton(themeDelegate)
        binding.radioGroupThemePicker.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                FutureRes.id.radioButtonSystemTheme -> themeDelegate.setSystemMode()
                FutureRes.id.radioButtonLightTheme -> themeDelegate.setLightMode()
                FutureRes.id.radioButtonNightTheme -> themeDelegate.setNightMode()
            }
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
