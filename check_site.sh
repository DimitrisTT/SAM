#!/usr/bin/env bash
curl -s -o "/dev/null" localhost:8080 || sudo systemdctl restart scheduler && sudo systemdctl restart schedulerapi
