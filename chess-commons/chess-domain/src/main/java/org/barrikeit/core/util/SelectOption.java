package org.barrikeit.core.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record SelectOption(String label, Object value) {}
