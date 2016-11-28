typedef BOOL (__stdcall *pInitHardWare)();

typedef BOOL (__stdcall *pUnintHWare)();

typedef BOOL (__stdcall *pIsSDKCorrect)();

typedef BOOL (__stdcall *pKZGetVideoSize)(USHORT *width, USHORT *height);

typedef BOOL (__stdcall *pKZPreviewStartVideo)();

typedef BOOL (__stdcall *pKZPreviewOneVideoFrameRGB)(LPVOID buffer);

typedef BOOL (__stdcall *pKZPreviewStopVideo)();

typedef BOOL (__stdcall *pKZSetUploadMode)();

typedef BOOL (__stdcall *pKZSetNormalStoreMode)();

typedef DWORD (__stdcall *pKZUpload)(LPVOID buffer);

typedef BOOL (__stdcall *pKZCapture)();

typedef BOOL (__stdcall *pKZFlashModeSet)(USHORT flash_mode);

typedef BOOL (__stdcall *pKZFireOutsideFlash)();

typedef BOOL (__stdcall *pKZFirePower_OutsideFlash)(USHORT duration);

typedef BOOL (__stdcall *pKZIsUseCaptureAF)(USHORT is_use);

typedef BOOL (__stdcall *pKZAFMode)(USHORT mode);

typedef BOOL (__stdcall *pKZFlashCharge)(USHORT charge);

typedef BOOL (__stdcall *pKZEnterPMode)();

typedef BOOL (__stdcall *pKZEnterAMode)();

typedef BOOL (__stdcall *pKZEnterSMode)();

typedef BOOL (__stdcall *pKZEnterMMode)();

typedef BOOL (__stdcall *pKZASM_AVSET)(USHORT mode);

typedef BOOL (__stdcall *pKZASM_TVSET)(USHORT mode);

typedef BOOL (__stdcall *pKZDisablePowerSave)();

typedef BOOL (__stdcall *pKZEnablePowerSave)();

typedef BOOL (__stdcall *pKZLcdOn)();

typedef BOOL (__stdcall *pKZLcdOff)();

typedef BOOL (__stdcall *pKZKEYOn)();

typedef BOOL (__stdcall *pKZKEYOff)();

typedef BOOL (__stdcall *pKZLensClose)();

typedef BOOL (__stdcall *pKZLensOut)();

typedef BOOL (__stdcall *pKZPowerOff)();

typedef DWORD (__stdcall *pKZGetZoomPos)();

typedef BOOL (__stdcall *pKZZoom)(USHORT zoom_number);

typedef DWORD (__stdcall *pKZGetFocusPos)();

typedef BOOL (__stdcall *pKZFocusPos)(USHORT focus_pos);

typedef DWORD (__stdcall *pKZIsLensMoving)();

typedef DWORD (__stdcall *pKZIsFlashCharging)();

typedef BOOL (__stdcall *pKZImageSizeSet)(USHORT image_size);

typedef BOOL (__stdcall *pKZSetQuality)(USHORT quality);

typedef BOOL (__stdcall *pKZBiasSet)(USHORT bias);

typedef BOOL (__stdcall *pKZAWBMode)(USHORT mode, USHORT effect);

typedef BOOL (__stdcall *pKZISOSet)(USHORT iso);

typedef BOOL (__stdcall *pKZMeterMode)(USHORT meter);

typedef BOOL (__stdcall *pKZSharpMode)(USHORT sharp);

typedef BOOL (__stdcall *pKZModify_para)(USHORT item, USHORT data);

typedef BOOL (__stdcall *pKZIdle)();

typedef BOOL (__stdcall *pKZPreview)();

typedef BOOL (__stdcall *pKZInitDSCParam)();

typedef BOOL (__stdcall *pKZAFAreaSet)(USHORT area);

typedef BOOL (__stdcall *pKZAF)();

typedef BOOL (__stdcall *pKZIsDoingAF)();

typedef BOOL (__stdcall *pKZSwitchOSDOnOff)(USHORT switcher);

typedef BOOL (__stdcall *pKZAWBCustomRGB)(USHORT red, USHORT green, USHORT blue);

typedef BOOL (__stdcall *pKZGetWBCustomRGB)(USHORT *red, USHORT *green, USHORT *blue);

typedef DWORD (__stdcall *pKZVersion)();

typedef DWORD (__stdcall *pKZIsImgInDSC)();

typedef DWORD (__stdcall *pKZIsFocusMoving)();

typedef DWORD (__stdcall *pKZGetZoomStep)();

pInitHardWare InitHardWare = NULL;
pUnintHWare UnintHWare = NULL;
pIsSDKCorrect IsSDKCorrect = NULL;
pKZGetVideoSize KZGetVideoSize = NULL;
pKZPreviewStartVideo KZPreviewStartVideo = NULL;
pKZPreviewOneVideoFrameRGB KZPreviewOneVideoFrameRGB = NULL;
pKZPreviewStopVideo KZPreviewStopVideo = NULL;
pKZSetUploadMode KZSetUploadMode = NULL;
pKZSetNormalStoreMode KZSetNormalStoreMode = NULL;
pKZUpload KZUpload = NULL;
pKZCapture KZCapture = NULL;
pKZFlashModeSet KZFlashModeSet = NULL;
pKZFireOutsideFlash KZFireOutsideFlash = NULL;
pKZFirePower_OutsideFlash KZFirePower_OutsideFlash = NULL;
pKZIsUseCaptureAF KZIsUseCaptureAF = NULL;
pKZAFMode KZAFMode = NULL;
pKZFlashCharge KZFlashCharge = NULL;
pKZEnterPMode KZEnterPMode = NULL;
pKZEnterAMode KZEnterAMode = NULL;
pKZEnterSMode KZEnterSMode = NULL;
pKZEnterMMode KZEnterMMode = NULL;
pKZASM_AVSET KZASM_AVSET = NULL;
pKZASM_TVSET KZASM_TVSET = NULL;
pKZDisablePowerSave KZDisablePowerSave = NULL;
pKZEnablePowerSave KZEnablePowerSave = NULL;
pKZLcdOn KZLcdOn = NULL;
pKZLcdOff KZLcdOff = NULL;
pKZKEYOn KZKEYOn = NULL;
pKZKEYOff KZKEYOff = NULL;
pKZLensClose KZLensClose = NULL;
pKZLensOut KZLensOut = NULL;
pKZPowerOff KZPowerOff = NULL;
pKZGetZoomPos KZGetZoomPos = NULL;
pKZZoom KZZoom = NULL;
pKZGetFocusPos KZGetFocusPos = NULL;
pKZFocusPos KZFocusPos = NULL;
pKZIsLensMoving KZIsLensMoving = NULL;
pKZIsFlashCharging KZIsFlashCharging = NULL;
pKZImageSizeSet KZImageSizeSet = NULL;
pKZSetQuality KZSetQuality = NULL;
pKZBiasSet KZBiasSet = NULL;
pKZAWBMode KZAWBMode = NULL;
pKZISOSet KZISOSet = NULL;
pKZMeterMode KZMeterMode = NULL;
pKZSharpMode KZSharpMode = NULL;
pKZModify_para KZModify_para = NULL;
pKZIdle KZIdle = NULL;
pKZPreview KZPreview = NULL;
pKZInitDSCParam KZInitDSCParam = NULL;
pKZAFAreaSet KZAFAreaSet = NULL;
pKZAF KZAF = NULL;
pKZIsDoingAF KZIsDoingAF = NULL;
pKZSwitchOSDOnOff KZSwitchOSDOnOff = NULL;
pKZAWBCustomRGB KZAWBCustomRGB = NULL;
pKZGetWBCustomRGB KZGetWBCustomRGB = NULL;
pKZVersion KZVersion = NULL;
pKZIsImgInDSC KZIsImgInDSC = NULL;
pKZIsFocusMoving KZIsFocusMoving = NULL;
pKZGetZoomStep KZGetZoomStep = NULL;