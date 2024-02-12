package com.example.retrofitapp.presentation.filter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.retrofitapp.R
import com.example.retrofitapp.databinding.FilterBottomSheetBinding
import com.example.retrofitapp.domain.model.character.FilterCharacters
import com.example.retrofitapp.domain.model.character.Gender
import com.example.retrofitapp.domain.model.character.Status
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class FilterBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FilterBottomSheetBinding

    private var selectedStatus: Status? = null
    private var selectedGender: Gender? = null

    private var filterCharacters = FilterCharacters()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterCharacters = arguments?.getSerializableCompat(FILTER_KEY, FilterCharacters::class.java)!!

        for (valueStatus in Status.values()) {
            val radioButton = RadioButton(context)
            radioButton.text = valueStatus.name
            radioButton.id = valueStatus.ordinal
            binding.statusRadioGroup.addView(radioButton)
        }
        binding.statusRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedStatus = Status.values()[checkedId]
        }

        for (valueGender in Gender.values()) {
            val radioButton = RadioButton(context)
            radioButton.text = valueGender.name
            radioButton.id = valueGender.ordinal
            binding.genderRadioGroup.addView(radioButton)
        }
        binding.genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedGender = Gender.values()[checkedId]
        }

        setupApplyButton()
        setupCancelButton()

        binding.searchName.setText(filterCharacters.name)
        binding.searchSpecies.setText(filterCharacters.species)
        filterCharacters.status?.ordinal?.let { binding.statusRadioGroup.check(it) }
        filterCharacters.gender?.ordinal?.let { binding.genderRadioGroup.check(it) }

    }

    private fun setupApplyButton() {
        binding.applyButton.setOnClickListener {
            filterCharacters.name = binding.searchName.text.toString()
            filterCharacters.species = binding.searchSpecies.text.toString()
            filterCharacters.status = selectedStatus
            filterCharacters.gender = selectedGender

            if (selectedStatus == null || selectedGender == null) {
                Toast.makeText(context, getString(R.string.comment_filter), Toast.LENGTH_SHORT).show()
            } else {
                setFragmentResult(REQUEST_KEY, bundleOf("key" to filterCharacters))
                dismiss()
            }
        }

    }

    private fun setupCancelButton(){
        binding.cancelButton.setOnClickListener {
            filterCharacters.name = ""
            filterCharacters.species = ""
            filterCharacters.status = null
            filterCharacters.gender = null

            setFragmentResult(REQUEST_KEY, bundleOf(PAIRS_KEY to null))
            dismiss()
        }
    }

    private fun <T : Serializable?> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializable(key, clazz)!!
        else (getSerializable(key) as T)
    }

    companion object{
        const val FILTER_KEY = "FilterCharacters"
        const val REQUEST_KEY = "SetCancelFilter"
        const val PAIRS_KEY = "key"
    }
}
