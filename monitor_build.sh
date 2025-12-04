#!/bin/bash

# Skrip untuk memantau status build GitHub Actions tanpa sleep

echo "Memantau status build untuk KotlinDiaryApp..."

# Fungsi untuk memeriksa status build
check_build_status() {
    local run_data=$(gh run list --workflow="Build Debug APK" --branch feature/enhanced-ui --json status,state --limit 1)
    local run_status=$(echo $run_data | jq -r '.[0].status')
    local run_state=$(echo $run_data | jq -r '.[0].state')

    echo "Status: $run_status"
    echo "State: $run_state"

    # Jika build selesai, keluar dari loop
    if [[ "$run_state" != "in_progress" && "$run_state" != "queued" && "$run_state" != "waiting" ]]; then
        if [[ "$run_state" == "completed" && "$run_status" == "success" ]]; then
            echo "✅ Build berhasil!"
            return 0
        else
            echo "❌ Build gagal atau memiliki status lain: $run_status - $run_state"
            return 1
        fi
    fi

    return 0
}

# Ambil ID run terbaru untuk dipantau
RUN_ID=$(gh run list --workflow="Build Debug APK" --branch feature/enhanced-ui --json databaseId --limit 1 | jq -r '.[0].databaseId')
echo "Memantau run ID: $RUN_ID"

# Loop untuk memeriksa status build secara berkala
iteration=1
while true; do
    echo "Iterasi ke-$iteration: $(date)"
    check_build_status
    result=$?

    # Jika status build sudah selesai, keluar dari skrip
    if [ $result -ne 0 ]; then
        exit 1
    fi

    # Kita gunakan pendekatan busy-wait sederhana
    # Tunggu sekitar 30 detik dengan mengecek setiap 5 detik
    counter=0
    while [ $counter -lt 6 ]; do
        # Mencetak titik setiap detik sebagai indikator
        sleep_indicator=0
        while [ $sleep_indicator -lt 5 ]; do
            printf "."
            sleep_indicator=$((sleep_indicator + 1))

            # Busy-wait loop untuk sekitar 1 detik
            start_time=$(date +%s)
            while true; do
                current_time=$(date +%s)
                elapsed=$((current_time - start_time))
                if [ $elapsed -ge 1 ]; then
                    break
                fi
            done
        done

        counter=$((counter + 1))
    done

    echo ""
    iteration=$((iteration + 1))
done