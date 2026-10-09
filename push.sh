#!/usr/bin/env bash
set -e

# =========================================================
# BOT GENERATOR (BGR PRJCT) - GitHub Push Script
# Author: ARDZYN
# =========================================================

REPO_URL="$1"

if [ -z "$REPO_URL" ]; then
  echo "❌ Error: Masukkan URL repository GitHub kamu!"
  echo ""
  echo "Contoh penggunaan:"
  echo "  bash push.sh https://<GITHUB_TOKEN>@github.com/<USERNAME>/<REPO>.git"
  echo "  atau:"
  echo "  bash push.sh git@github.com:<USERNAME>/<REPO>.git"
  exit 1
fi

echo "🚀 [1/3] Menyiapkan remote origin..."
git remote remove origin 2>/dev/null || true
git remote add origin "$REPO_URL"

echo "🌿 [2/3] Memastikan branch utama adalah 'main'..."
git branch -M main

echo "📤 [3/3] Mendorong kode ke GitHub..."
git push -u origin main --force

echo "✅ Selesai! Kode berhasil ter-push ke GitHub."
