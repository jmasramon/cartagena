# cartagena

The game in clojure.
Based on the work from Mark Bastian at https://github.com/markbastian/pirates. I have re-done everything except the visual gui part that is just Mark's.

## Environment Setup

This project requires **Java 8**. Your system is already configured correctly.

### Quick Start
```bash
# Run the game
lein run

# Run tests
lein test

# Check environment
./check-env.sh
```

### Java Version Management
If you need to switch between Java versions:
```bash
# Use the project script to ensure Java 8
./set-java8.sh lein run

# Or manually set Java 8 as default
sudo update-alternatives --config java
```

## Usage

The game launches with a Swing GUI. Right-click on squares with pieces to make moves.

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
