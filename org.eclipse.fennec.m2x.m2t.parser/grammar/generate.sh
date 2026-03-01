#!/bin/bash
# Generates M2tLexer and M2tParser from split ANTLR4 grammars.
# Called by bnd -generate. Arguments: $1=classpath $2=output-dir $3=grammar-dir
set -e
CP="$1"
OUTDIR="$2"
GRAMMARDIR="$3"
PKG=org.eclipse.fennec.m2x.m2t.parser

mkdir -p "$OUTDIR"

# Step 1: Lexer (produces M2tLexer.tokens in OUTDIR)
java -cp "$CP" org.antlr.v4.Tool \
    -no-listener \
    -package "$PKG" \
    -o "$OUTDIR" \
    "$GRAMMARDIR/M2tLexer.g4"

# Step 2: Parser (reads M2tLexer.tokens from -lib OUTDIR)
java -cp "$CP" org.antlr.v4.Tool \
    -visitor -no-listener \
    -lib "$OUTDIR" \
    -package "$PKG" \
    -o "$OUTDIR" \
    "$GRAMMARDIR/M2tParser.g4"
