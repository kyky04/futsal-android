package uinbdg.skirpsi.futsal.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pragmadev on 3/12/18.
 */

public class AccessTokenRequest {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("client_id")
    @Expose
    private String client_id;
    @SerializedName("client_secret")
    @Expose
    private String client_secret;
    @SerializedName("grant_type")
    @Expose
    private String grant_type;
    @SerializedName("scope")
    @Expose
    private String scope;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }





    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }


}