/**
 * Generated by orval v6.24.0 🍺
 * Do not edit manually.
 * infrastructure API
 * OpenAPI spec version: 1.0-SNAPSHOT
 */
import type { DebugFeature } from "./debugFeature";
import type { ExecuteStructuredReferences } from "./executeStructuredReferences";

export interface Execute {
  debugFeature: DebugFeature;
  formula: string;
  structuredReferences: ExecuteStructuredReferences;
}
