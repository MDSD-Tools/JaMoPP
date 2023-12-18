package org.palladiosimulator.dependencytool.dependencies;

/**
 * The type of P2Repository to use.
 */
public enum UpdateSiteTypes {
    NIGHTLY, RELEASE;
    
    @Override
    public String toString() {
        switch(this) {
            case NIGHTLY: return "nightly";
            case RELEASE: return "release";
            default: throw new IllegalArgumentException();
        }
    }
}
