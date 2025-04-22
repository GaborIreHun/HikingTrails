# HikingTrails
Program that reads a topographic grid (rows of digits 0–9), finds every “trailhead” (0), explores all +1‑step paths up to summits (9) using breadth‑first search, and prints each trailhead’s reachable‑9 count plus their total score.

Note: This utility is intended as a demonstration of the core algorithmic approach and therefore expects strictly well‑formed input (each line a uniform length of digits 0–9, at least one 0 and one 9, terminated by END when used interactively). It is not hardened as a production‑grade parser; please ensure your map data conforms to the specification before running.

**Prerequisites**  
- Java 17 or newer (uses `record` types).

---

**Compile**  
```bash
javac HikingTrails.java
```

---

**Usage**

Feed your digit‑map in two simple ways:

1. **Redirect from a file**  
   ```bash
   java HikingTrails < map.txt
   ```  
   where `map.txt` contains the eight rows:
   ```
   89010123
   78121874
   87430965
   96549874
   45678903
   32019012
   01329801
   10456732
   ```

2. **Pipe / paste directly at the CLI**  
   ```bash
   echo -e "89010123\n78121874\n87430965\n96549874\n45678903\n32019012\n01329801\n10456732\nEND" \
     | java HikingTrails
   ```  
   Or run interactively:
   ```bash
   java HikingTrails
   ```
   then type (or paste) each of the eight rows above, one per line, and finish with:
   ```
   END
   ```

---

**Output**  
```text
Trailheads: [5, 6, 5, 3, 1, 3, 5, 3, 5]
Score:      36
```

- **Trailheads**: the per‑zero list of how many distinct 9’s each trailhead can reach (in reading order).  
- **Score**: the sum of those counts (36).
