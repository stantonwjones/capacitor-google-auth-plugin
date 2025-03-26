export interface CapacitorGoogleAuthPlugin {
    authorize(options: {
        scopes: [string];
        clientId: string;
    }): Promise<{
        accessToken?: string;
        idToken?: string;
    }>;
}
export interface User {
    /**
     * The unique identifier for the user.
     */
    id: string;
    /**
     * The email address associated with the user.
     */
    email: string;
    /**
     * The user's full name.
     */
    name: string;
    /**
     * The family name (last name) of the user.
     */
    familyName: string;
    /**
     * The given name (first name) of the user.
     */
    givenName: string;
    /**
     * The URL of the user's profile picture.
     */
    imageUrl: string;
    /**
     * The server authentication code.
     */
    serverAuthCode: string;
    /**
     * The authentication details including access, refresh and ID tokens.
     */
    accessToken: string;
    idToken?: string;
    refreshToken?: string;
}
