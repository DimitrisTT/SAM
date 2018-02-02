package com.tracktik.scheduler.domain;

public class KeyValueFact {

  private String key;
  private Object value;

  public String getKey() {
    return key;
  }

  public KeyValueFact setKey(String key) {
    this.key = key;
    return this;
  }

  public Object getValue() {
    return value;
  }

  public KeyValueFact setValue(Object value) {
    this.value = value;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    KeyValueFact that = (KeyValueFact) o;

    if (key != null ? !key.equals(that.key) : that.key != null) return false;
    return value != null ? value.equals(that.value) : that.value == null;
  }

  @Override
  public int hashCode() {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "KeyValueFact{" +
        "key='" + key + '\'' +
        ", value=" + value +
        '}';
  }
}
