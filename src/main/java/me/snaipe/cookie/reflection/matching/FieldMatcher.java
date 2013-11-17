package me.snaipe.cookie.reflection.matching;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import me.snaipe.cookie.reflection.util.Classes;

public class FieldMatcher {
	
	private final static int ALL_FIELDS_MASK = Modifier.FINAL | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.PUBLIC | Modifier.STATIC | Modifier.TRANSIENT | Modifier.VOLATILE;

	private Class<?> clazz;
	private boolean strict = false;
	private int mask = ALL_FIELDS_MASK;
	private String nameRegex;
	
	private Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
	private Class<?> type;

	private boolean deep;
	private boolean includeObject;

	private Field[] fields;
	
	public FieldMatcher(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public FieldMatcher withMask(int mask) {
		this.mask = mask;
		this.fields = null;
		return this;
	}
	
	public FieldMatcher usingStrictSearch() {
		this.strict = true;
		this.fields = null;
		return this;
	}
	
	@SafeVarargs
	public final FieldMatcher withAnnotations(Class<? extends Annotation>... annotations) {
		this.annotations.clear();
		Collections.addAll(this.annotations, annotations);
		this.fields = null;
		return this;
	}
	
	public FieldMatcher withName(String regex) {
		this.nameRegex = regex;
		this.fields = null;
		return this;
	}
	
	public FieldMatcher ofType(Class<?> type) {
		this.type = type;
		this.fields = null;
		return this;
	}
	
	public FieldMatcher notSearchingSuperclasses() {
		this.deep = false;
		this.fields = null;
		return this;
	}
	
	public FieldMatcher includingObject() {
		this.includeObject = true;
		this.fields = null;
		return this;
	}
	
	public Field one() {
		Iterator<Field> it = all().iterator();
		if (it.hasNext()) {
			return all().iterator().next();
		}
		return null;
	}

	public Collection<Field> all() {
		populateFields();
		
		Set<Field> fields = new HashSet<Field>();
		
		for (Field f : this.fields) {
			addMatchingField(f, fields);
		}
		
		return Collections.unmodifiableCollection(fields);
	}
	
	private void populateFields() {
		if (this.fields == null) {
			this.fields = Classes.getAllFields(clazz, deep, includeObject);
		}
	}
	
	private void addMatchingField(Field field, Set<Field> fields) {
		if (strict ? (field.getModifiers() ^ mask) == 0 : (field.getModifiers() & mask) != 0) {
			if (annotations.isEmpty() || Classes.hasAnnotations(field, annotations)) {
				if (type == null || field.getType().equals(type)) {
					if (nameRegex == null || field.getName().matches(nameRegex)) {
						fields.add(field);
					}
				}
			}
		}
	}
}
