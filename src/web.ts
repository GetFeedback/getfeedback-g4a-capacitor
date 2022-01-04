import { WebPlugin } from '@capacitor/core';

import type { GetFeedbackCapacitorPlugin } from './definitions';

export class GetFeedbackCapacitorWeb
  extends WebPlugin
  implements GetFeedbackCapacitorPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
