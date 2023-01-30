import { WebPlugin } from '@capacitor/core';

import type {
  GetFeedbackCapacitorPlugin,
  standardEventsCallback,
  CallbackID,
} from './definitions';

export class GetFeedbackCapacitorWeb
  extends WebPlugin
  implements GetFeedbackCapacitorPlugin {
  initialize(options: { appID: string }): void {
    console.log('initialize', options);
    throw new Error('Method not implemented.');
  }

  async standardEvents(callback: standardEventsCallback): Promise<CallbackID> {
    console.log('standardEvents', callback);
    throw new Error('Method not implemented.');
  }
  loadFeedbackForm(options: { formID: string }): Promise<any> {
    console.log('loadFeedbackForm', options);
    throw new Error('Method not implemented.');
  }
  sendEvent(options: { eventName: string }): Promise<any> {
    console.log('sendEvent', options);
    throw new Error('Method not implemented.');
  }
  resetCampaignData(): void {
    throw new Error('Method not implemented.');
  }
  removeCachedForms(): void {
    throw new Error('Method not implemented.');
  }
  setDebugEnabled(options: { debugEnabled: boolean }): void {
    console.log('setDebugEnabled', options);
    throw new Error('Method not implemented.');
  }
  loadLocalizedStringFile(options: { localizedStringFile: string }): void {
    console.log('loadLocalizedStringFile', options);
    throw new Error('Method not implemented.');
  }
  loadFeedbackFormWithCurrentViewScreenshot(options: {
    formID: string;
  }): Promise<any> {
    console.log('loadFeedbackFormWithCurrentViewScreenshot', options);
    throw new Error('Method not implemented.');
  }
  preloadFeedbackForms(options: { formIDs: string[] }): void {
    console.log('preloadFeedbackForms', options);
    throw new Error('Method not implemented.');
  }
  setCustomVariables(options: { customVariables: any }): void {
    console.log('setCustomVariables', options);
    throw new Error('Method not implemented.');
  }
  dismiss(): void {
    throw new Error('Method not implemented.');
  }
  setDataMasking(options: { masks: string[]; character: string }): void {
    console.log('setDataMasking', options);
    throw new Error('Method not implemented.');
  }
  getDefaultDataMasks(): Promise<any> {
    throw new Error('Method not implemented.');
  }
}
