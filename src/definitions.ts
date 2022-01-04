export interface GetFeedbackCapacitorPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
