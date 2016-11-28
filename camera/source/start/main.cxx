#include <windows.h>
#include <dirent.h>
#include <cstdlib>
#include <ctime>
#include <iostream>
#include <fstream>
#include <iomanip>
#include <c++/sstream>
#include "../../header/unit.h"

using General = bool (__stdcall *)();
using FlashMode = bool (__stdcall *)(unsigned short);
using VideoSize = bool (__stdcall *)(unsigned short *, unsigned short *);
using Upload = unsigned long (__stdcall *)(void *);

std::string default_store = "I:/Cache/Camera/";

void format_current_date(std::stringstream &buffer, const char *format) {
    time_t t = time(nullptr);
    tm tm = *localtime(&t);
    buffer.str("");
    buffer << std::put_time(&tm, format);
    std::cout << "Current date is: " << buffer.str() << std::endl;
}

void process_image(const Upload upload) {
    unsigned int *buffer;
    try {
        buffer = new unsigned int[1 << 23];
        unsigned long image_size = (*upload)(buffer);
        if (image_size) {
            std::cout << "The image size camera capture is: " << image_size << std::endl;
            std::stringstream content;
            format_current_date(content, "%Y%m%d");
            std::string target = default_store.append(content.str());
            if (!opendir(target.c_str())) {
                mkdir(target.c_str());
                std::cout << "Make directory: [" << target << "] success !" << std::endl;
            }
            content.clear(std::ios_base::goodbit);
            format_current_date(content, "%H%M%S");
            std::string time_str{content.str()};
            std::ofstream picture(target.append("/").append(time_str.append(".jpeg")),
                                  std::ios_base::out | std::ios_base::binary);
            if (picture.is_open()) {
                picture.write(reinterpret_cast<char *>(buffer), static_cast<std::streamsize>(image_size));
            }
            delete[] buffer;
        }

    } catch (std::exception e) {
        std::cout << "Exception happened by root cause: " << e.what() << std::endl;
        delete[] buffer;
    }

}

bool camera_operation() {
    HINSTANCE library = LoadLibrary("F:/Project/Work/external/camera_x64.dll");
    if (library) {
        /* get pointer to the function in the dll*/
        General process = reinterpret_cast<General> (GetProcAddress(library, "InitHardWare"));
        if (!process || !(*process)()) {
            FreeLibrary(library);
            std::cout << "Initialize camera failed!" << GetLastError() << std::endl;
            return -false;
        }

        process = reinterpret_cast<General> (GetProcAddress(library, "IsSDKCorrect"));
        if (!process || !(*process)()) {
            FreeLibrary(library);
            std::cout << "Camera's SDK is incorrect!" << GetLastError() << std::endl;
            return false;
        }

        process = reinterpret_cast<General> (GetProcAddress(library, "KZDisablePowerSave"));
        if (!process || !(*process)()) {
            FreeLibrary(library);
            std::cout << "Disable camera's power saved failed!" << GetLastError() << std::endl;
            return false;
        }

        process = reinterpret_cast<General> (GetProcAddress(library, "KZInitDSCParam"));
        if (!process || !(*process)()) {
            FreeLibrary(library);
            std::cout << "Initialize camera's DSC param failed!" << GetLastError() << std::endl;
            return false;
        }

        FlashMode function = reinterpret_cast<FlashMode> (GetProcAddress(library, "KZFlashModeSet"));
        if (!function || !(*function)(4)) {
            std::cout << "Function call failed: KZFlashModeSet(" << 4 << "), cause root: "
            << GetLastError() << std::endl;
            FreeLibrary(library);
            return false;
        }

        process = reinterpret_cast<General> (GetProcAddress(library, "KZEnterPMode"));
        if (!process || !(*process)()) {
            std::cout << "Function call failed: KZEnterPMode(), cause root: " << GetLastError() << std::endl;
            FreeLibrary(library);
            return false;
        }

        Sleep(1000);

        unsigned short width = 0, height = 0;
        VideoSize getVideoSize = reinterpret_cast<VideoSize> (GetProcAddress(library, "KZGetVideoSize"));
        if (!getVideoSize || !(*getVideoSize)(&width, &height)) {
            std::cout << "Function call failed: KZGetVideoSize(" << width << "," << height << "), cause root: " <<
            GetLastError() << std::endl;
            FreeLibrary(library);
            return false;
        }

        process = reinterpret_cast<General> (GetProcAddress(library, "KZSetUploadMode"));
        if (!process || !(*process)()) {
            std::cout << "Function call failed: KZSetUploadMode(), cause root: " << GetLastError() << std::endl;
            FreeLibrary(library);
            return false;
        }

        Sleep(1000);
        process = reinterpret_cast<General> (GetProcAddress(library, "KZPreviewStartVideo"));
        if (!process || !(*process)()) {
            std::cout << "Function call failed: KZPreviewStartVideo(), cause root: " << GetLastError() << std::endl;
            FreeLibrary(library);
            return false;
        }

        Sleep(1500);
        process = reinterpret_cast<General> (GetProcAddress(library, "KZPreviewStopVideo"));
        if (!process || !(*process)()) {
            std::cout << "Function call failed: KZPreviewStopVideo(), cause root: " << GetLastError() << std::endl;
            FreeLibrary(library);
            return false;
        }

        process = reinterpret_cast<General> (GetProcAddress(library, "KZCapture"));
        if (!process || !(*process)()) {
            std::cout << "Function call failed: KZCapture(), cause root: " << GetLastError() << std::endl;
            FreeLibrary(library);
            return false;
        } else {
            Sleep(2000);
            Upload upload = reinterpret_cast<Upload> (GetProcAddress(library, "KZUpload"));
            if (upload) process_image(upload);
        }
        FreeLibrary(library);
    } else {
        FreeLibrary(library);
    }
    return true;
}

int main() {
    cst::show_form();
    cst::show_quite();
    if (!camera_operation()) {
        return 0;
    } else {
        return 1;
    }
}