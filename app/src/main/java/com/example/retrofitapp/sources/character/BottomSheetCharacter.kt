package com.example.retrofitapp.sources.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.example.retrofitapp.databinding.FilterCharacterBinding
import com.example.retrofitapp.sources.character.data.FilterCharacter
import com.example.retrofitapp.sources.character.data.Gender
import com.example.retrofitapp.sources.character.data.Status
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCharacter : BottomSheetDialogFragment() {
    private lateinit var binding: FilterCharacterBinding

    private lateinit var selectedStatus: Status
    private lateinit var selectedGender: Gender

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilterCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupApplyButton()

        selectedStatus = Status.values().first()
        selectedGender = Gender.values().first()

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
    }

    private fun setupApplyButton() {
        binding.applyButton.setOnClickListener {
            val name = binding.searchName.text.toString()
            val species = binding.searchSpecies.text.toString()
            val filter = FilterCharacter(
                name = name,
                species = species,
                status = selectedStatus,
                gender = selectedGender
            )
            (activity?.supportFragmentManager?.fragments?.firstOrNull { it is CharacterFragment } as? CharacterFragment)?.setFilter(filter)
            dismiss()
        }
    }
}
