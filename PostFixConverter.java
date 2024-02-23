package com.tns.dsa.algo;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PostFixConverter {
	
	private Stack<Character> stack = new Stack<Character>();
	private Map<Character, Integer> precedent = new HashMap<Character, Integer>();

	{
		precedent.put('(', 1);
		precedent.put('+', 2);
		precedent.put('-', 2);
		precedent.put('*', 3);
		precedent.put('/', 3);
		precedent.put('^', 4);
		precedent.put(')', 5);
	}
	
	public String convert(String infix) {
		String postfix = "";
		int length = infix.length();
		for (int i = 0; i < length; i++) {
			char c = infix.charAt(i);
			if (isOperand(c)) {
				postfix = postfix.concat(Character.toString(c));
			} else if (isOperator(c)) {
				// Most things will happen here
				
				// stack is empty so simply push to stack
				if (stack.isEmpty()) {
					stack.push(c);
				}
				// ( is the weakest precedent so we can simply push to top 
				else if (c == '(') {
					stack.push(c);
				} 
				// ) means we need to solve until we see (
				else if (c == ')') {
					
					// process until we see ')'
					while(stack.peek() != '(') {
						postfix = postfix.concat(Character.toString(stack.pop()));
					}
					// popping ( because we process until we see ( so finally ( is left so we need to pop it out
					stack.pop();
				} else {
					/*
					 * This is the process for current char is operator and there is another operator in the stack
					 * here precedent will be working
					 */
					
					// current operator is greater than the peeked one
					if (compareOperator(c, stack.peek()) == 1) {
						stack.push(c);
					} else {
						// need to pop till current is is equal or less than peeked operator
						while (!stack.isEmpty() && (compareOperator(c, stack.peek()) == 0 || compareOperator(c, stack.peek()) == -1)) {
							// break means we don't handle "(" because "(" means there is ")" so we let above finding ")" condition to pop "("
							if (stack.peek() == '(')
								break;
							
							// we concat to postfix because this is popping 
							postfix = postfix.concat(Character.toString(stack.pop()));
						}
						
						// finally, current operator is push to the stack 
						stack.push(c);
					}
				}
			}

		}
		
		
		// popping the rest of the item in stack
		while (!stack.isEmpty()) {
			postfix = postfix.concat(Character.toString(stack.pop()));
		}
		return postfix;
	}

	int getPrecendent(char c) {
		return precedent.get(c);
	}

	boolean isOperand(char a) {
		int charValue = a;
		/*
		 *  65 - 90 means UTF-8 decimal for A To Z
		 *  97 - 122 means UTF-8 decimal for a to z
		 */
		return (charValue >= 65 && charValue <= 90) || (charValue >= 97 && charValue <= 122);
	}

	boolean isOperator(char a) {
		int charValue = a;
		switch (charValue) {
		// using UTF-8 in decimal forms 
		case 40: // (
		case 41: // )
		case 42: // * 
		case 43: // +
		case 45: // -
		case 47: // /
		case 94: // ^
			return true;
		default:
			return false;
		}
	}

	int compareOperator(char a, char b) {
		/*
		 * 1 means a > b
		 * 0 means a == b
		 * -1 means a < b 
		 */
		if (precedent.get(a) > precedent.get(b)) {
			return 1;
		} else if (precedent.get(a) < precedent.get(b)) {
			return -1;
		} else {
			return 0;
		}
	}
}
