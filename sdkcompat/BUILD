# Description: Indirections for SDK changes to the underlying platform library.

load("//intellij_platform_sdk:build_defs.bzl", "select_for_plugin_api")

licenses(["notice"])  # Apache 2.0

package_group(
    name = "visibility",
    includes = [
        "//common:common_plugins",
    ],
    packages = [
        "//common/...",
        "//testing",
    ],
)

package(default_visibility = [":visibility"])

java_library(
    name = "sdkcompat",
    visibility = ["//visibility:public"],
    exports = select_for_plugin_api({
        "android-studio-4.0": ["//sdkcompat/v193"],
        "android-studio-4.1": ["//sdkcompat/v193"],
        "intellij-2019.3": ["//sdkcompat/v193"],
        "intellij-ue-2019.3": ["//sdkcompat/v193"],
        "intellij-2020.1": ["//sdkcompat/v201"],
        "intellij-ue-2020.1": ["//sdkcompat/v201"],
        "clion-2019.3": ["//sdkcompat/v193"],
        "clion-2020.1": ["//sdkcompat/v201"],
    }),
)
