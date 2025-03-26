import { registerPlugin } from '@capacitor/core';
const CapacitorGoogleAuth = registerPlugin('CapacitorGoogleAuth', {
    web: () => import('./web').then((m) => new m.CapacitorGoogleAuthWeb()),
});
export * from './definitions';
export { CapacitorGoogleAuth };
//# sourceMappingURL=index.js.map