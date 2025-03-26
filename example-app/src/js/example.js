import { CapacitorGoogleAuth } from '@projectinvicta/capacitor-google-auth';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    CapacitorGoogleAuth.authorize({ scopes: ['email', 'profile'], clientId: "875126660467-0989oohd5e2uf9vcrqo8drtj7cj4f6f0.apps.googleusercontent.com" }).then(result => console.log(result));
}
