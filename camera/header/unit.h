//
// Created by Developer on 12/16/2015.
//

#ifndef CAMERA_FUNCTION_H
#define CAMERA_FUNCTION_H

#include <nana/gui.hpp>
#include <nana/gui/wvl.hpp>
#include <nana/gui/widgets/label.hpp>
#include <nana/gui/widgets/button.hpp>
#include <nana/gui/place.hpp>

using namespace nana;
namespace cst {
    struct stateinfo {
        enum class state {
            init, operated, assigned
        };

        state opstate{state::init};
        wchar_t operation{L'+'};
        double oprand{0};
        double outcome{0};
        label &procedure;
        label &result;

        stateinfo(label &proc, label &resl)
                : procedure(proc), result(resl) { }
    };

    void numkey_pressed(stateinfo &state, const arg_mouse &arg);

    void opkey_pressed(stateinfo &state, const arg_mouse &arg);

    void go();

    void show_form();

    void show_quite();
}


#endif //CAMERA_FUNCTION_H
