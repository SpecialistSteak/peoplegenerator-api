"""Plot throughput and latency against the measured request concurrency."""
import json
from pathlib import Path

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.ticker import FuncFormatter, FixedLocator, NullLocator

root = Path(__file__).resolve().parent
results = json.loads((root / "results.json").read_text())["results"]
levels = [row for row in results if row["name"] == "single person"]
concurrency = [row["concurrency"] for row in levels]
throughput = [row["successfulPerSecond"] for row in levels]
background, ink, muted = "#faf9fc", "#252137", "#625b72"
purple, amber = "#7655b1", "#a65b18"
plt.rcParams.update({"font.family": "DejaVu Sans", "font.size": 11})
figure, (capacity, latency) = plt.subplots(2, 1, figsize=(10, 8), dpi=180, sharex=True)
figure.patch.set_facecolor(background)
figure.subplots_adjust(left=0.12, right=0.94, top=0.81, bottom=0.16, hspace=0.47)
figure.text(0.12, 0.94, "Throughput and response time under load", fontsize=20, weight="bold", color=ink)
figure.text(0.12, 0.895, "A new person per request · 2 CPU cores · 2 GiB memory limit", fontsize=11, color=muted)

for axis in (capacity, latency):
    axis.set_facecolor(background)
    axis.set_xscale("log", base=2)
    axis.set_xlim(0.85, 155)
    axis.xaxis.set_major_locator(FixedLocator(concurrency))
    axis.xaxis.set_major_formatter(FuncFormatter(lambda value, _: str(int(value))))
    axis.xaxis.set_minor_locator(NullLocator())
    axis.grid(axis="y", color="#e3deeb", linewidth=0.8)
    axis.tick_params(length=0, colors=muted, pad=8)
    axis.axvline(32, color="#b9adce", linewidth=1, linestyle="--", zorder=0)
    for spine in axis.spines.values():
        spine.set_visible(False)

capacity.set_title("Completed requests", loc="left", fontsize=13, weight="bold", color=ink, pad=14)
capacity.plot(concurrency, throughput, color=purple, marker="o", markersize=6, linewidth=2.3)
capacity.fill_between(concurrency, throughput, color=purple, alpha=0.08)
capacity.set_ylabel("Successful requests / sec", color=muted, labelpad=12)
capacity.set_ylim(0, 29000)
capacity.set_yticks([0, 10000, 20000])
capacity.yaxis.set_major_formatter(FuncFormatter(lambda value, _: "0" if not value else f"{value / 1000:g}k"))
capacity.annotate("23.6k requests/s at 32 concurrent requests", xy=(32, throughput[3]), xytext=(0.28, 1.04), textcoords="axes fraction", color=purple, fontsize=11, arrowprops={"arrowstyle":"-", "color":purple}, ha="left")
capacity.annotate("18.5k", xy=(128, throughput[-1]), xytext=(0, -23), textcoords="offset points", color=purple, ha="center", weight="bold")

latency.set_title("Response time", loc="left", fontsize=13, weight="bold", color=ink, pad=14)
latency.plot(concurrency, [row["p95"] for row in levels], color=purple, marker="o", markersize=5, linewidth=2, label="95th percentile")
latency.plot(concurrency, [row["p99"] for row in levels], color=amber, marker="o", markersize=5, linewidth=2, label="99th percentile")
latency.set_yscale("log")
latency.set_ylim(0.25, 500)
latency.set_yticks([1, 10, 100])
latency.yaxis.set_major_formatter(FuncFormatter(lambda value, _: str(int(value))))
latency.yaxis.set_minor_locator(NullLocator())
latency.set_ylabel("Milliseconds (log scale)", color=muted, labelpad=12)
latency.set_xlabel("Concurrent requests (log scale)", color=muted, labelpad=14)
latency.legend(loc="upper left", frameon=False, labelcolor=muted, fontsize=10)
latency.annotate("2.7 ms", xy=(16, 2.7), xytext=(-9, 12), textcoords="offset points", color=amber, ha="right", fontsize=10)
latency.annotate("201 ms", xy=(128, 201.4), xytext=(-8, 14), textcoords="offset points", color=amber, ha="right", fontsize=10, weight="bold")
figure.text(0.12, 0.055, "20-second runs after warm-up · GET /api/person/ · Local measurements", fontsize=10, color=muted)
figure.savefig(root / "throughput.png", facecolor=background)
plt.close(figure)
