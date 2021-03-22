package com.lemust.repository.models.rest.auth.reset

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChangePsswordDTO(@SerializedName("old_password") @Expose var oldPassword: String? = null,
                       @SerializedName("new_password1") @Expose var newPassword1: String? = null,
                       @SerializedName("new_password2") @Expose var newPassword2: String? = null)
