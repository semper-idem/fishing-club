package net.semperidem.fishingclub.fish.specimen;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.semperidem.fishingclub.fish.specimen.SpecimenData.MAX_QUALITY;
import static net.semperidem.fishingclub.fish.specimen.SpecimenData.MIN_QUALITY;

public class SpecimenDescription {
    private final SpecimenData data;
    private final List<Line> description;
    private final List<Text> asTooltip;

    private SpecimenDescription(SpecimenData data) {
        this.data = data;
        this.description = new ArrayList<>();
        this.description.add(this.getName());
        this.description.add(this.getSpecies());
        this.description.add(this.getWeight());
        this.description.add(this.getLength());
        this.description.add(this.getQuality());
        this.description.add(this.getCaughtBy());
        this.description.add(this.getCaughtAt());
        this.asTooltip = List.copyOf(this.description).stream().map(Line::joined).toList();
    }

    public static SpecimenDescription of(SpecimenData data) {
        return new SpecimenDescription(data);
    }

    private Line getName() {
        int grade = 3;
        if (this.data.isAlbino()) {
            grade++;
        }
        if (this.data.weird()) {
            grade++;
        }
        return new Line(
                MutableText.of(PlainTextContent.of("Name:")),
                MutableText.of(PlainTextContent.of(this.data.label())).setStyle(forGrade(grade))
        );
    }

    private Line getSpecies() {
        Species<?> species = this.data.species();
        int grade = (int) MathHelper.clamp(Math.ceil(Math.log10(species.rarity())) + 1, 1, 6);
        return new Line(
                MutableText.of(PlainTextContent.of("Species:")),
                MutableText.of(PlainTextContent.of(species.label())).setStyle(forGrade(grade))
        );
    }

    private Line getWeight() {
        return new Line(
                MutableText.of(PlainTextContent.of("Weight:")),
                MutableText.of(PlainTextContent.of(String.format("%.2f", this.data.weight()))).setStyle(forGrade(percentileGrade(this.data.weightPercentile())))
        );
    }

    private Line getLength() {
        return new Line(
                MutableText.of(PlainTextContent.of("Length:")),
                MutableText.of(PlainTextContent.of(String.format("%.2f", this.data.length()))).setStyle(forGrade(percentileGrade(this.data.lengthPercentile())))
        );
    }

    private Line getQuality() {
        String qualityString = switch(this.data.quality()) {
            case MIN_QUALITY -> "Defective";
            case 2 -> "Poor";
            case 3 -> "Standard";
            case 4 -> "Good";
            case 5 -> "Excellent";
            case MAX_QUALITY -> "Perfect";
            default -> "Unidentifiable";
        };
        return new Line(
                MutableText.of(PlainTextContent.of("Quality:")),
                MutableText.of(PlainTextContent.of(qualityString)).setStyle(forGrade(this.data.quality()))
        );
    }

    private Line getCaughtBy() {
        return new Line(
                MutableText.of(PlainTextContent.of("Caught by:")),
                MutableText.of(PlainTextContent.of(this.data.caughtBy())).setStyle(STANDARD)
        );
    }

    private Line getCaughtAt() {
        return new Line(
                MutableText.of(PlainTextContent.of("Caught at:")),
                MutableText.of(PlainTextContent.of(Utils.epochToFormatted(this.data.caughtAt()))).setStyle(STANDARD)
        );
    }

    public List<Line> get() {
        return this.description;
    }

    public List<Text> asTooltip() {
        return this.asTooltip;
    }

    private static Style forGrade(int grade) {
        return switch (grade) {
            case 1 -> DEFECTIVE;
            case 2 -> POOR;
            case 3 -> STANDARD;
            case 4 -> GOOD;
            case 5 -> EXCELLENT;
            case 6 -> PERFECT;
            default -> Style.EMPTY;
        };
    }

    private static int percentileGrade(float percentile) {
        if (percentile < 0.25) {
            return MIN_QUALITY;
        }
        if (percentile < 0.5) {
            return 2;
        }
        if (percentile < 0.8) {
            return 3;
        }
        if (percentile < 0.9) {
            return 4;
        }
        if (percentile < 0.99) {
            return 5;
        }
        return MAX_QUALITY;
    }

    private static final Style DEFECTIVE = Style.EMPTY.withColor(Formatting.DARK_GRAY);
    private static final Style POOR = Style.EMPTY.withColor(Formatting.GRAY);
    private static final Style STANDARD = Style.EMPTY.withColor(Formatting.WHITE);
    private static final Style GOOD = Style.EMPTY.withColor(Formatting.DARK_GREEN);
    private static final Style EXCELLENT = Style.EMPTY.withColor(Formatting.DARK_AQUA);
    private static final Style PERFECT = Style.EMPTY.withColor(Formatting.GOLD).withBold(true);

    public static class Line extends Pair<MutableText, MutableText> {
        public Line(MutableText left, MutableText right) {
            super(left, right);
        }

        /**
         * Used in Items tooltip for now
         * but we should have custom tooltip renderer
         * so each left is aligned left and each right is aligned right
         * */
        public Text joined() {
            return this.getLeft().copy().append(" ").append(this.getRight());
        }
    }
}
