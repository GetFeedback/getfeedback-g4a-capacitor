import { registerPlugin } from '@capacitor/core';

import type { GetFeedbackCapacitorPlugin } from './definitions';

const GetFeedbackCapacitor = registerPlugin<GetFeedbackCapacitorPlugin>(
  'GetFeedbackCapacitor',
  {
    web: () => import('./web').then(m => new m.GetFeedbackCapacitorWeb()),
  },
);

export * from './definitions';
export { GetFeedbackCapacitor };
