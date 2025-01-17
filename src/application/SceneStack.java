//SceneStack Class: manages a stack of scenes for navigation and scene transitions

package application;

import java.util.Stack;

import javafx.scene.Scene;

public class SceneStack {

	// for single instance of the SceneStack
	private static SceneStack instance = null;
	private static Stack<Scene> stack = new Stack<Scene>();

	//constructor
	public SceneStack() {
		
	}

	//returns the single instance of SceneStack
	public static SceneStack getInstance() {
		if (instance == null) {
			instance = new SceneStack();
		}
		return instance;
	}
	
	//add scene
	public void push(Scene s) {
		stack.push(s);
	}

	//remove scene
	public void pop() {
		stack.pop();
	}

	//return top of stack
	public Scene peek() {
		return stack.peek();
	}
}
