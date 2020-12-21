package com.spudg.spudginsuranceorganiser

import android.app.Dialog
import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_tag.*
import kotlinx.android.synthetic.main.dialog_add_tag.*
import kotlinx.android.synthetic.main.dialog_add_tag.view.*
import kotlinx.android.synthetic.main.dialog_delete_tag.*
import kotlinx.android.synthetic.main.dialog_update_tag.*
import kotlinx.android.synthetic.main.dialog_update_tag.view.*

class TagActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        setUpTagList()

        add_tag.setOnClickListener {
            addTag()
        }

        back_to_policies_from_tags.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun getTagsList(): ArrayList<TagModel> {
        val dbHandler = TagHandler(this, null)
        val result = dbHandler.getAllTags()
        dbHandler.close()
        return result
    }


    private fun setUpTagList() {
        if (getTagsList().size >= 0) {
            rvTags.layoutManager = LinearLayoutManager(this)
            val tagsAdapter = TagAdapter(this, getTagsList())
            rvTags.adapter = tagsAdapter
        }
    }

    private fun addTag() {
        val addDialog = Dialog(this, R.style.Theme_Dialog)
        addDialog.setCancelable(false)
        addDialog.setContentView(R.layout.dialog_add_tag)
        addDialog.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))

        addDialog.colourPickerAddTag.showOldCenterColor = false

        addDialog.tvAddTag.setOnClickListener {
            val name = addDialog.etNameLayoutAddTag.etNameAddTag.text.toString()
            Constants.TAG_COLOUR_SELECTED = addDialog.colourPickerAddTag.color
            val colour = Constants.TAG_COLOUR_SELECTED.toString()

            val dbHandler = TagHandler(this, null)

            if (title.isNotEmpty() && colour.isNotEmpty()) {
                dbHandler.addCategory(TagModel(0, name, colour))
                if (Constants.TAG_UNIQUE_TITLE == 1) {
                    Toast.makeText(this, "Tag added.", Toast.LENGTH_LONG).show()
                    setUpTagList()
                    addDialog.dismiss()
                } else {
                    Toast.makeText(this, "Tag name already exists.", Toast.LENGTH_LONG).show()
                }

                setUpTagList()


            } else {
                Toast.makeText(this, "Name or colour can't be blank.", Toast.LENGTH_LONG).show()
            }

            dbHandler.close()

        }

        addDialog.tvCancelAddTag.setOnClickListener {
            addDialog.dismiss()
        }

        addDialog.show()
    }

    fun updateTag(tag: TagModel) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update_tag)
        updateDialog.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))

        updateDialog.etNameLayoutUpdateTag.etNameUpdateTag.setText(tag.name)

        updateDialog.colourPickerUpdate.color = tag.colour.toInt()
        updateDialog.colourPickerUpdate.showOldCenterColor = false

        updateDialog.tvUpdateUpdateTag.setOnClickListener {
            val name = updateDialog.etNameLayoutUpdateTag.etNameUpdateTag.text.toString()
            Constants.TAG_COLOUR_SELECTED = updateDialog.colourPickerUpdate.color
            val colour = Constants.TAG_COLOUR_SELECTED.toString()

            val dbHandler = TagHandler(this, null)

            if (title.isNotEmpty() && colour.isNotEmpty()) {
                dbHandler.updateTag(TagModel(tag.id, name, colour))
                if (Constants.TAG_UNIQUE_TITLE == 1) {
                    Toast.makeText(this, "Tag updated.", Toast.LENGTH_LONG).show()
                    setUpTagList()
                    updateDialog.dismiss()
                } else {
                    Toast.makeText(this, "Tag name already exists.", Toast.LENGTH_LONG).show()
                }
                setUpTagList()
            } else {
                Toast.makeText(this, "Name or colour can't be blank.", Toast.LENGTH_LONG).show()
            }

            dbHandler.close()

        }

        updateDialog.tvCancelUpdateTag.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

    fun deleteTag(tag: TagModel) {
        val deleteDialog = Dialog(this, R.style.Theme_Dialog)
        deleteDialog.setCancelable(false)
        deleteDialog.setContentView(R.layout.dialog_delete_tag)
        deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(TRANSPARENT))

        deleteDialog.tvDeleteDeleteTag.setOnClickListener {
            val dbHandlerTags = TagHandler(this, null)
            val dbHandlerPolicies = PolicyHandler(this, null)
            dbHandlerTags.deleteTag(TagModel(tag.id, "", ""))
            dbHandlerPolicies.changePolicyTagDueToTagDeletion(
                TagModel(
                    tag.id,
                    "",
                    ""
                )
            )

            Toast.makeText(this, "Tag deleted.", Toast.LENGTH_LONG).show()
            setUpTagList()
            dbHandlerTags.close()
            dbHandlerPolicies.close()
            deleteDialog.dismiss()
        }

        deleteDialog.tvCancelDeleteTag.setOnClickListener {
            deleteDialog.dismiss()
        }

        deleteDialog.show()

    }


}