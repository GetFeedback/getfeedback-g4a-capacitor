# getfeedback-g4a-capacitor

GetFeedback Capacitor is wrapper for GetFeedback SDK.

## Install

```bash
npm install getfeedback-capacitor
npx cap sync
```

## API

<docgen-index>

* [`initialize(...)`](#initialize)
* [`setDebugEnabled(...)`](#setdebugenabled)
* [`setCustomVariables(...)`](#setcustomvariables)
* [`loadFeedbackForm(...)`](#loadfeedbackform)
* [`loadFeedbackFormWithCurrentViewScreenshot(...)`](#loadfeedbackformwithcurrentviewscreenshot)
* [`sendEvent(...)`](#sendevent)
* [`resetCampaignData()`](#resetcampaigndata)
* [`dismiss()`](#dismiss)
* [`loadLocalizedStringFile(...)`](#loadlocalizedstringfile)
* [`preloadFeedbackForms(...)`](#preloadfeedbackforms)
* [`removeCachedForms()`](#removecachedforms)
* [`setDataMasking(...)`](#setdatamasking)
* [`getDefaultDataMasks()`](#getdefaultdatamasks)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initialize(...)

```typescript
initialize(options: { appID: string; }) => void
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ appID: string; }</code> |

--------------------


### setDebugEnabled(...)

```typescript
setDebugEnabled(options: { debugEnabled: boolean; }) => void
```

| Param         | Type                                    |
| ------------- | --------------------------------------- |
| **`options`** | <code>{ debugEnabled: boolean; }</code> |

--------------------


### setCustomVariables(...)

```typescript
setCustomVariables(options: { customVariables: any; }) => void
```

| Param         | Type                                   |
| ------------- | -------------------------------------- |
| **`options`** | <code>{ customVariables: any; }</code> |

--------------------


### loadFeedbackForm(...)

```typescript
loadFeedbackForm(options: { formID: string; }) => Promise<any>
```

| Param         | Type                             |
| ------------- | -------------------------------- |
| **`options`** | <code>{ formID: string; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### loadFeedbackFormWithCurrentViewScreenshot(...)

```typescript
loadFeedbackFormWithCurrentViewScreenshot(options: { formID: string; }) => Promise<any>
```

| Param         | Type                             |
| ------------- | -------------------------------- |
| **`options`** | <code>{ formID: string; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### sendEvent(...)

```typescript
sendEvent(options: { eventName: string; }) => Promise<any>
```

| Param         | Type                                |
| ------------- | ----------------------------------- |
| **`options`** | <code>{ eventName: string; }</code> |

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### resetCampaignData()

```typescript
resetCampaignData() => void
```

--------------------


### dismiss()

```typescript
dismiss() => void
```

--------------------


### loadLocalizedStringFile(...)

```typescript
loadLocalizedStringFile(options: { localizedStringFile: string; }) => void
```

| Param         | Type                                          |
| ------------- | --------------------------------------------- |
| **`options`** | <code>{ localizedStringFile: string; }</code> |

--------------------


### preloadFeedbackForms(...)

```typescript
preloadFeedbackForms(options: { formIDs: string[]; }) => void
```

| Param         | Type                                |
| ------------- | ----------------------------------- |
| **`options`** | <code>{ formIDs: string[]; }</code> |

--------------------


### removeCachedForms()

```typescript
removeCachedForms() => void
```

--------------------


### setDataMasking(...)

```typescript
setDataMasking(options?: { masks?: string[] | undefined; character?: string | undefined; } | undefined) => void
```

| Param         | Type                                                   |
| ------------- | ------------------------------------------------------ |
| **`options`** | <code>{ masks?: string[]; character?: string; }</code> |

--------------------


### getDefaultDataMasks()

```typescript
getDefaultDataMasks() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------

</docgen-api>
