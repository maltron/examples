//
//  AppDelegate.m
//  TestNetWorkActivity
//
//  Created by Mauricio Leal on 2/15/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "AppDelegate.h"
#import "MyViewController.h"

@implementation AppDelegate
@synthesize window = _window;

-(BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    _window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    MyViewController *viewController = [[MyViewController alloc] init];
    UINavigationController *navigation = [[UINavigationController alloc] initWithRootViewController:viewController];
    [self.window setRootViewController:navigation];
    [self.window makeKeyAndVisible];
    
    return YES;
}

@end
