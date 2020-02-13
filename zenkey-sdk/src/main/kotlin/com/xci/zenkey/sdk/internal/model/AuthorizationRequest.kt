package com.xci.zenkey.sdk.internal.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.xci.zenkey.sdk.BuildConfig

import com.xci.zenkey.sdk.internal.Json
import com.xci.zenkey.sdk.internal.ktx.appendQueryParameterIfNotNull
import com.xci.zenkey.sdk.internal.ktx.readNullableString
import com.xci.zenkey.sdk.internal.ktx.writeNullableString
import com.xci.zenkey.sdk.param.ResponseType

class AuthorizationRequest
    : Parcelable {

    internal val clientId: String?
    internal val redirectUri: Uri
    internal val scope: String?
    internal val state: String?
    internal val acr: String?
    internal val nonce: String?
    internal val prompt: String?
    internal val correlationId: String?
    internal val context: String?
    internal val proofKeyForCodeExchange: ProofKeyForCodeExchange
    private var loginHintToken: String? = null

    internal constructor(clientId: String,
                         redirectUri: Uri,
                         scope: String?,
                         state: String?,
                         acr: String?,
                         nonce: String?,
                         prompt: String?,
                         correlationId: String?,
                         context: String?,
                         proofKeyForCodeExchange: ProofKeyForCodeExchange) {
        this.clientId = clientId
        this.scope = scope
        this.state = state
        this.redirectUri = redirectUri
        this.acr = acr
        this.nonce = nonce
        this.prompt = prompt
        this.correlationId = correlationId
        this.context = context
        this.proofKeyForCodeExchange = proofKeyForCodeExchange
    }

    private constructor(parcel: Parcel) {
        clientId = parcel.readString()!!
        proofKeyForCodeExchange = parcel.readParcelable(javaClass.classLoader)!!
        redirectUri = parcel.readParcelable(javaClass.classLoader)!!
        scope = parcel.readNullableString()
        state = parcel.readNullableString()
        acr = parcel.readNullableString()
        nonce = parcel.readNullableString()
        prompt = parcel.readNullableString()
        correlationId = parcel.readNullableString()
        context = parcel.readNullableString()
        loginHintToken = parcel.readNullableString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(clientId)
        dest.writeParcelable(proofKeyForCodeExchange, flags)
        dest.writeParcelable(redirectUri, flags)
        dest.writeNullableString(scope)
        dest.writeNullableString(state)
        dest.writeNullableString(acr)
        dest.writeNullableString(nonce)
        dest.writeNullableString(prompt)
        dest.writeNullableString(correlationId)
        dest.writeNullableString(context)
        dest.writeNullableString(loginHintToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    internal fun withLoginHintToken(loginHintToken: String?): AuthorizationRequest {
        this.loginHintToken = loginHintToken
        return this
    }

    internal fun toDiscoverUiUri(endpoint: String)
            : Uri = Uri.parse(endpoint)
            .buildUpon()
            .appendQueryParameter(Json.KEY_SDK_VERSION, BuildConfig.VERSION_NAME)
            .appendQueryParameter(Json.KEY_CLIENT_ID, clientId)
            .appendQueryParameter(Json.KEY_REDIRECT_URI, redirectUri.toString())
            .appendQueryParameterIfNotNull(Json.KEY_STATE, state)
            .build()


    internal fun toAuthorizationUri(endpoint: String)
            : Uri = Uri.parse(endpoint)
            .buildUpon()
            .appendQueryParameter(Json.KEY_SDK_VERSION, BuildConfig.VERSION_NAME)
            .appendQueryParameter(Json.KEY_CLIENT_ID, clientId)
            .appendQueryParameter(Json.KEY_RESPONSE_TYPE, ResponseType.CODE.type)
            .appendQueryParameter(Json.KEY_REDIRECT_URI, redirectUri.toString())
            .appendQueryParameter(Json.KEY_CODE_CHALLENGE, proofKeyForCodeExchange.codeChallenge)
            .appendQueryParameter(Json.KEY_CODE_CHALLENGE_METHOD, proofKeyForCodeExchange.codeChallengeMethod.value)
            .appendQueryParameterIfNotNull(Json.KEY_SCOPE, scope)
            .appendQueryParameterIfNotNull(Json.KEY_STATE, state)
            .appendQueryParameterIfNotNull(Json.KEY_PROMPT, prompt)
            .appendQueryParameterIfNotNull(Json.KEY_NONCE, nonce)
            .appendQueryParameterIfNotNull(Json.KEY_ACR_VALUES, acr)
            .appendQueryParameterIfNotNull(Json.KEY_CORRELATION_ID, correlationId)
            .appendQueryParameterIfNotNull(Json.KEY_CONTEXT, context)
            .appendQueryParameterIfNotNull(Json.KEY_LOGIN_HINT_TOKEN, loginHintToken)
            .build()

    override fun toString(): String {
        return "AuthorizationRequest(" +
                "clientId=$clientId, " +
                "redirectUri=$redirectUri, " +
                "scope=$scope, " +
                "state=$state, " +
                "acr=$acr, " +
                "nonce=$nonce," +
                " prompt=$prompt, " +
                "correlationId=$correlationId, " +
                "context=$context, " +
                "proofKeyForCodeExchange=$proofKeyForCodeExchange, " +
                "loginHintToken=$loginHintToken)"
    }


    internal companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AuthorizationRequest> = object : Parcelable.Creator<AuthorizationRequest> {
            override fun createFromParcel(`in`: Parcel): AuthorizationRequest {
                return AuthorizationRequest(`in`)
            }

            override fun newArray(size: Int): Array<AuthorizationRequest?> {
                return arrayOfNulls(size)
            }
        }
    }
}


