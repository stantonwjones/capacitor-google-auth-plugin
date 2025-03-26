'use strict';

var core = require('@capacitor/core');

const CapacitorGoogleAuth = core.registerPlugin('CapacitorGoogleAuth', {
    web: () => Promise.resolve().then(function () { return web; }).then((m) => new m.CapacitorGoogleAuthWeb()),
});

class CapacitorGoogleAuthWeb extends core.WebPlugin {
    constructor() {
        super();
        this.googleApiLoadedSuccessfully = () => { };
        this.googleApiFailedToLoad = () => { };
        this.googleApiLoaded = new Promise((resolve, reject) => {
            this.googleApiLoadedSuccessfully = () => {
                resolve();
            };
            this.googleApiFailedToLoad = () => reject();
        });
        console.log("constructor called");
        this.loadScript();
    }
    async authorize(options) {
        await this.googleApiLoaded;
        return new Promise(async (resolve, reject) => {
            try {
                google.accounts.id.initialize({
                    client_id: options.clientId,
                    callback: (googleUser) => {
                        console.log(googleUser);
                        resolve({ idToken: googleUser.credential });
                    },
                });
                google.accounts.id.prompt();
            }
            catch (error) {
                reject(error);
            }
        });
    }
    // async refresh() {
    //   const authResponse = await gapi.auth2.getAuthInstance().currentUser.get().reloadAuthResponse();
    //   return {
    //     accessToken: authResponse.access_token,
    //     idToken: authResponse.id_token,
    //     refreshToken: '',
    //   };
    // }
    // async signOut() {
    //   return gapi.auth2.getAuthInstance().signOut();
    // }
    // async addUserChangeListener() {
    //   await this.gapiLoaded;
    //   gapi.auth2.getAuthInstance().currentUser.listen((googleUser) => {
    //     this.notifyListeners('userChange', googleUser.isSignedIn() ? this.getUserFrom(googleUser) : null);
    //   });
    // }
    getUserFrom(googleUser) {
        const user = {};
        const profile = googleUser.getBasicProfile();
        user.email = profile.getEmail();
        user.familyName = profile.getFamilyName();
        user.givenName = profile.getGivenName();
        user.id = profile.getId();
        user.imageUrl = profile.getImageUrl();
        user.name = profile.getName();
        const authResponse = googleUser.getAuthResponse(true);
        user.accessToken = authResponse.access_token;
        user.idToken = authResponse.id_token;
        user.refreshToken = '';
        return user;
    }
    // Dynamically loads google api scripts
    loadScript() {
        if (typeof document === 'undefined') {
            return;
        }
        const scriptId = 'gapi';
        const scriptEl = document === null || document === void 0 ? void 0 : document.getElementById(scriptId);
        if (scriptEl) {
            return;
        }
        const head = document.getElementsByTagName('head')[0];
        const script = document.createElement('script');
        script.type = 'text/javascript';
        script.defer = true;
        script.async = true;
        script.id = scriptId;
        script.onload = () => this.googleApiLoadedSuccessfully();
        script.src = 'https://accounts.google.com/gsi/client';
        head.appendChild(script);
    }
}

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    CapacitorGoogleAuthWeb: CapacitorGoogleAuthWeb
});

exports.CapacitorGoogleAuth = CapacitorGoogleAuth;
//# sourceMappingURL=plugin.cjs.js.map
