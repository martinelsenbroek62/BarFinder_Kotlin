package com.lemust.repository.models.filters
import com.google.gson.annotations.SerializedName


    

data class FilterData(
		@SerializedName("id") var id: Long,
		@SerializedName("name") var name: String?,
		@SerializedName("field_type") var fieldType: String?,
		@SerializedName("is_shown_in_short_description") var isShownInShortDescription: Boolean?,
		@SerializedName("options") var options: List<OptionDTO>? = null
)

//@SerializedName("options")
//@Expose
//private val options: Any? = null