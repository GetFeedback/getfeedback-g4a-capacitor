export type standardEventsCallback = (response: any, error?: any) => void;

export type CallbackID = string;

export interface GetFeedbackCapacitorPlugin {
  initialize(options: { appID: string }): void;
  standardEvents(callback: standardEventsCallback): Promise<CallbackID>;
  setDebugEnabled(options: { debugEnabled: boolean }): void;
  setCustomVariables(options: { customVariables: any }): void;
  loadFeedbackForm(options: { formID: string }): Promise<any>;
  loadFeedbackFormWithCurrentViewScreenshot(options: {
    formID: string;
  }): Promise<any>;
  sendEvent(options: { eventName: string }): Promise<any>;
  resetCampaignData(): void;
  dismiss(): void;
  loadLocalizedStringFile(options: { localizedStringFile: string }): void;
  preloadFeedbackForms(options: { formIDs: string[] }): void;
  removeCachedForms(): void;
  setDataMasking(options?: { masks?: string[]; character?: string }): void;
  getDefaultDataMasks(): Promise<any>;
}
