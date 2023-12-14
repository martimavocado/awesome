#!/bin/bash

working=$(basename "$(pwd)")
if ls build/libs/"$working"* 1> /dev/null 2>&1; then
    if ls ~/.var/app/org.prismlauncher.PrismLauncher/data/PrismLauncher/instances/dev/.minecraft/mods/"$working"* 1> /dev/null 2>&1; then
        rm ~/.var/app/org.prismlauncher.PrismLauncher/data/PrismLauncher/instances/dev/.minecraft/mods/"$working"*
        mv build/libs/"$working"* ~/.var/app/org.prismlauncher.PrismLauncher/data/PrismLauncher/instances/dev/.minecraft/mods/
    else
        mv build/libs/"$working"* ~/.var/app/org.prismlauncher.PrismLauncher/data/PrismLauncher/instances/dev/.minecraft/mods/
    fi
    flatpak run org.prismlauncher.PrismLauncher -l dev
else
    if ls ~/.var/app/org.prismlauncher.PrismLauncher/data/PrismLauncher/instances/dev/.minecraft/mods/"$working"* 1> /dev/null 2>&1; then
        echo "$working found, no changes needed."
    else
        echo "$working not found"
        exit 1
    fi
fi
