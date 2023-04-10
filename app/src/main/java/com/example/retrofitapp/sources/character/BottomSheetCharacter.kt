package com.example.retrofitapp.sources.character

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.example.retrofitapp.databinding.FilterCharacterBinding
import com.example.retrofitapp.sources.character.data.FilterCharacter
import com.example.retrofitapp.sources.character.data.Gender
import com.example.retrofitapp.sources.character.data.Status
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class BottomSheetCharacter : BottomSheetDialogFragment() {
    private lateinit var binding: FilterCharacterBinding

    private var selectedStatus: Status? = null
    private var selectedGender: Gender? = null

    private var filterCharacter = FilterCharacter()

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
        filterCharacter = arguments?.getSerializableCompat("FilterCharacter", FilterCharacter::class.java)!!


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

        binding.searchName.setText(filterCharacter.name)
        binding.searchSpecies.setText(filterCharacter.species)

        filterCharacter.status?.ordinal?.let { binding.statusRadioGroup.check(it) }
        filterCharacter.gender?.ordinal?.let { binding.genderRadioGroup.check(it) }

    }

    private fun setupApplyButton() {
        binding.applyButton.setOnClickListener {
            filterCharacter.name = binding.searchName.text.toString()
            filterCharacter.species = binding.searchSpecies.text.toString()
            filterCharacter.status = selectedStatus
            filterCharacter.gender = selectedGender

            if (selectedStatus == null || selectedGender == null) {
                Toast.makeText(context, "You need to select both status and gender", Toast.LENGTH_SHORT).show()
            } else {
                (activity?.supportFragmentManager?.fragments?.firstOrNull { it is CharacterFragment } as? CharacterFragment)?.setFilter(filterCharacter)
                dismiss()
            }
        }

    }

    private fun setupCancelButton(){
        binding.cancelButton.setOnClickListener {
            filterCharacter.name = ""
            filterCharacter.species = ""
            filterCharacter.status = null
            filterCharacter.gender = null

            (activity?.supportFragmentManager?.fragments?.firstOrNull { it is CharacterFragment } as? CharacterFragment)?.cancelFilter()
            dismiss()
        }
    }

    private fun <T : Serializable?> Bundle.getSerializableCompat(key: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializable(key, clazz)!!
        else (getSerializable(key) as T)
    }
}
