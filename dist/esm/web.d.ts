import { WebPlugin } from '@capacitor/core';
import type { CapacitorGoogleAuthPlugin } from './definitions';
export declare class CapacitorGoogleAuthWeb extends WebPlugin implements CapacitorGoogleAuthPlugin {
    googleApiLoadedSuccessfully: () => void;
    googleApiFailedToLoad: () => void;
    googleApiLoaded: Promise<void>;
    constructor();
    authorize(options: {
        scopes: [string];
        clientId: string;
    }): Promise<{
        accessToken: string;
    }>;
    private getUserFrom;
    private loadScript;
}
