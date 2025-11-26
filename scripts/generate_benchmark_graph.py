import os

def create_svg():
    # Data
    labels = ["Vanilla (Single)", "Masked (Single)"]
    values = [6300000, 1800000]
    max_val = max(values)
    
    # Dimensions
    width = 600
    height = 160
    bar_height = 40
    bar_gap = 30
    start_y = 40
    label_width = 150
    chart_width = width - label_width - 50
    
    svg_content = f'''<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}">
    <style>
        .label {{ font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif; font-size: 14px; fill: #333; }}
        .value {{ font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif; font-size: 14px; fill: #666; font-weight: bold; }}
        .title {{ font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif; font-size: 16px; fill: #333; font-weight: bold; }}
        .bar-bg {{ fill: #eee; rx: 4; }}
        .bar-vanilla {{ fill: #4CAF50; rx: 4; }}
        .bar-masked {{ fill: #2196F3; rx: 4; }}
    </style>
    
    <text x="10" y="25" class="title">Serialization Throughput (ops/s)</text>
    '''
    
    for i, (label, value) in enumerate(zip(labels, values)):
        y = start_y + i * (bar_height + bar_gap)
        bar_width = (value / max_val) * chart_width
        
        # Bar Background
        svg_content += f'<rect x="{label_width}" y="{y}" width="{chart_width}" height="{bar_height}" class="bar-bg" />\n'
        
        # Actual Bar
        color_class = "bar-vanilla" if i == 0 else "bar-masked"
        svg_content += f'<rect x="{label_width}" y="{y}" width="{bar_width}" height="{bar_height}" class="{color_class}" />\n'
        
        # Label
        svg_content += f'<text x="{label_width - 10}" y="{y + bar_height/2 + 5}" text-anchor="end" class="label">{label}</text>\n'
        
        # Value
        formatted_value = f"{value:,}"
        svg_content += f'<text x="{label_width + bar_width + 10}" y="{y + bar_height/2 + 5}" class="value">{formatted_value}</text>\n'

    svg_content += '</svg>'
    
    os.makedirs("docs/images", exist_ok=True)
    with open("docs/images/benchmark_graph.svg", "w") as f:
        f.write(svg_content)
    
    print("Generated docs/images/benchmark_graph.svg")

if __name__ == "__main__":
    create_svg()
