# HikingTrails
Program that reads a topographic grid (rows of digits 0–9), finds every “trailhead” (0), explores all +1‑step paths up to summits (9) using breadth‑first search, and prints each trailhead’s reachable‑9 count plus their total score.

**Prerequisites**  
- Java 17 or newer (uses `record` types).

---

**Compile**  
```bash
javac HikingTrails.java
```

---

**Usage**

Feed your digit‑map interactively:

   ```bash
   java HikingTrails
   ```
   You’ll see:
   Enter number of columns (positive integer):
   → type 8, Enter
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
