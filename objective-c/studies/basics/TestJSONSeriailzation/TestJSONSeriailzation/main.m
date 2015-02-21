//
//  main.m
//  TestJSONSeriailzation
//
//  Created by Mauricio Leal on 2/10/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    
    @autoreleasepool {
        NSString *RAW = @"[{\"ID\":1,\"firstName\":\"Mauricio\",\"lastName\":\"Leal\"},{\"ID\":2,\"firstName\":\"Nadia\",\"lastName\":\"Ulanova\"},{\"ID\":3,\"firstName\":\"Nichole\",\"lastName\":\"Leal\"}]";
        NSData *data = [RAW dataUsingEncoding:NSUTF8StringEncoding];
        
        NSError *error = nil;
        id json = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
        if(error) {
            NSLog(@"### ERROR:%@, %@", error, [error userInfo]);
        } else if([json isKindOfClass:[NSArray class]]) {
            NSLog(@"JSON is an NSArray");
            NSArray *array = (NSArray *)json;
            for(id component in array)
                NSLog(@"....Each component of a class:%@", [component class]);
            
        } else if([json isKindOfClass:[NSDictionary class]]) {
            NSLog(@"JSON is an NSDictionary");
            NSDictionary *dict = (NSDictionary *)json;
        }
        
        
//        NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
//        [dict setObject:@"4" forKey:@"ID"];
//        [dict setObject:@"Mauricio" forKey:@"firstName"];
//        [dict setObject:@"Leal" forKey:@"lastName"];
//        
//        NSError *error = nil;
//        NSData *data = [NSJSONSerialization dataWithJSONObject:dict options:kNilOptions error:nil];
//        if(error) {
//            NSLog(@"### Error:%@, %@", error, [error userInfo]);
//        } else {
//            NSLog(@"RESULT:%@",[[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]);
//        }
        
        
    }
    return 0;
}
