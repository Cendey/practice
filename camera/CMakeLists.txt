cmake_minimum_required(VERSION 3.0.0)
project(CAMERA)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
#
## nana
#set(NANA_ROOT "F:/Libarary/nana/build/bin/mingw")
#include_directories("${NANA_ROOT}/include")
#link_libraries("${NANA_ROOT}/lib")
#
##opencv
#
##find_package(OpenCV REQUIRED)
##include_directories(${OpenCV_INCLUDE_DIRS})
##target_link_libraries(CAMERA ${OpenCV_LIBS})
#
## boost
#set(BOOST_DIR "F:/Libarary/boost/build")
#set(BOOST_ROOT "F:/Libarary/boost/build")
#set(BOOST_INCLUDEDIR "${BOOST_ROOT}/include")
#set(BOOST_LIBRARYDIR "${BOOST_ROOT}/lib")
#set(Boost_NO_SYSTEM_PATHS true)
#set(Boost_USE_STATIC_LIBS OFF CACHE BOOL "use static libraries from Boost")
#set(Boost_USE_MULTITHREADED ON)
#find_package(Boost REQUIRED COMPONENTS system program_options thread filesystem date_time chrono timer regex serialization)
#
#if (Boost_FOUND)
#    include_directories(${BOOST_INCLUDEDIR})
#    link_libraries(${BOOST_LIBRARYDIR})
#
#    if (WIN32)
#        # disable autolinking in boost
#        add_definitions(-DBOOST_ALL_NO_LIB)
#        # force all boost libraries to dynamic link (we already disabled
#        # autolinking, so I don't know why we need this, but we do!)
#        add_definitions(-DBOOST_ALL_DYN_LINK)
#    endif (WIN32)
#endif (Boost_FOUND)

set(SOURCE_FILES source/start/main.cxx detail/unit.cpp header/unit.h)
add_executable(CAMERA ${SOURCE_FILES})
#target_link_libraries(CAMERA nana)