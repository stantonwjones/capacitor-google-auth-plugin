import { registerPlugin } from '@capacitor/core';

import type { CapacitorGoogleAuthPlugin } from './definitions';

const CapacitorGoogleAuth = registerPlugin<CapacitorGoogleAuthPlugin>('CapacitorGoogleAuth', {
  web: () => import('./web').then((m) => new m.CapacitorGoogleAuthWeb()),
});

export * from './definitions';
export { CapacitorGoogleAuth };
