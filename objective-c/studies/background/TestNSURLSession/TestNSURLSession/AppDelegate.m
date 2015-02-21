//
//  AppDelegate.m
//  TestNSURLSession
//
//  Created by Mauricio Leal on 2/3/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "AppDelegate.h"
#import "MyViewController.h"

@implementation AppDelegate

-(BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    _window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    MyViewController *viewController = [[MyViewController alloc] init];
    UINavigationController *navigation = [[UINavigationController alloc] initWithRootViewController:viewController];
    [self.window setRootViewController:navigation];
    [self.window makeKeyAndVisible];
    
    return YES;
}

-(void)application:(UIApplication *)application performFetchWithCompletionHandler:(void ((^)UIBackgroundFetchResult))completionHandler {
    
}

-

@end
